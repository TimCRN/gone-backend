package com.example.plugins

import com.example.Constants
import com.example.ErrorObject
import com.example.data.model.request.*
import com.example.data.model.response.ErrorResponse
import com.example.data.model.response.SimpleResponse
import com.example.data.model.user.NewUser
import com.example.repository.UserRepository
import com.example.services.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


fun Application.configureBasicRouting(
    userRepo: UserRepository
) {
    routing {
        authenticate("basic") {
            route("/api") {
                get("/test"){
                    call.respondText("You are authenticated!")
                }
                route("/users"){
                    get("/"){
                        call.respond(userRepo.getAll())
                    }
                }
                route("/account"){
                    get() {
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userEmail = principal!!.payload.getClaim("email").asString()
                            val account = userRepo.getAccountByEmail(userEmail)!!
                            call.respond(account)
                        } catch(e:Exception) {
                            call.respond(
                                HttpStatusCode.InternalServerError,
                                ErrorResponse(
                                    false,
                                    Constants.SERVER_ERROR
                                )
                            )
                        }
                    }
                    get("/picture") {
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userEmail = principal!!.payload.getClaim("email").asString()
                            val pictureId = userRepo.getProfilePictureId(userEmail)
                            val picture = GetFile(pictureId)
                            if(picture != null){
                                call.respond(
                                    HttpStatusCode.OK,
                                    SimpleResponse(
                                        true,
                                        picture
                                    )
                                )
                            } else {
                                call.respond(
                                    HttpStatusCode.NotFound,
                                    SimpleResponse(
                                        false,
                                        "File id $pictureId was not found"
                                    )
                                )
                            }

                        } catch (e:Exception) {
                            call.respond(
                                HttpStatusCode.InternalServerError,
                                ErrorResponse(
                                    false,
                                    e.message ?: Constants.SERVER_ERROR
                                )
                            )
                        }
                    }
                    post("/picture"){
                        val pictureUploadRequest = try{
                            call.receive<PictureUploadRequest>()
                        } catch (e:Exception) {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                SimpleResponse(false, Constants.INVALID_PARAMETERS)
                            )
                            return@post
                        }
                        try {
                            if(
                                pictureUploadRequest.image.contains("data:image") &&
                                pictureUploadRequest.image.contains("base64"))
                            {
                                val imageString = pictureUploadRequest.image.split("base64,")
                                val pictureId: UUID = UploadFile(imageString[1])
                                val principal = call.principal<JWTPrincipal>()
                                val userEmail = principal!!.payload.getClaim("email").asString()
                                userRepo.changeProfilePicture(userEmail,pictureId)
                                call.respond(
                                    HttpStatusCode.OK,
                                    SimpleResponse(true,pictureId.toString())
                                )
                            } else {
                                call.respond(
                                    HttpStatusCode.BadRequest,
                                    SimpleResponse(false,"Incorrect image format. Use Base64 string.")
                                )
                            }

                        } catch (e:Exception) {
                            call.respond(
                                HttpStatusCode.InternalServerError,
                                SimpleResponse(false, Constants.SERVER_ERROR)
                            )
                        }
                    }
                    patch("/username"){
                        val usernameChangeRequest = try{
                            call.receive<UsernameChangeRequest>()
                        } catch (e:Exception) {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                SimpleResponse(false, Constants.INVALID_PARAMETERS)
                            )
                            return@patch
                        }
                        try{
                            val principal = call.principal<JWTPrincipal>()
                            val userEmail = principal!!.payload.getClaim("email").asString()
                            val user = userRepo.getUserByEmail(userEmail)!!
                            if(userRepo.validatePassword(user,usernameChangeRequest.password)){
                                var tag = user.tag
                                while(!userRepo.checkTagAvailability(usernameChangeRequest.new_username, tag))
                                {
                                    tag = (0..9999).random()
                                }
                                userRepo.changeUsername(user, usernameChangeRequest.new_username, tag)
                                call.respond(
                                    HttpStatusCode.OK,
                                    SimpleResponse(true, "Username changed to ${usernameChangeRequest.new_username}#$tag")
                                )
                            } else {
                                call.respond(
                                    HttpStatusCode.Conflict,
                                    ErrorResponse(false,"Incorrect password")
                                )
                            }
                        } catch (e:Exception) {
                            call.respond(
                                HttpStatusCode.InternalServerError,
                                SimpleResponse(false, Constants.SERVER_ERROR)
                            )
                        }
                    }
                    patch("/password") {
                        val passwordChangeRequest = try{
                            call.receive<PasswordChangeRequest>()
                        } catch (e:Exception) {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                SimpleResponse(false, Constants.INVALID_PARAMETERS)
                            )
                            return@patch
                        }
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userEmail = principal!!.payload.getClaim("email").asString()
                            val user = userRepo.getUserByEmail(userEmail)!!
                            if(userRepo.validatePassword(user,passwordChangeRequest.old_password)){
                                userRepo.changePassword(user, passwordChangeRequest.new_password)
                                //TODO Invalidate old token and issue a new one
                                //TODO Notify user through email
                                call.respond(
                                    HttpStatusCode.OK,
                                    SimpleResponse(true, "Password was succesfully changed")
                                )
                            } else {
                                call.respond(
                                    HttpStatusCode.Conflict,
                                    ErrorResponse(false,"Incorrect password")
                                )
                            }
                        } catch (e:Exception) {
                            call.respond(
                                HttpStatusCode.InternalServerError,
                                ErrorResponse(false, e.message ?: Constants.SERVER_ERROR)
                            )
                        }
                    }
                    patch("/email"){
                        val emailChangeRequest = try{
                            call.receive<EmailChangeRequest>()
                        } catch (e:Exception) {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                SimpleResponse(false, Constants.INVALID_PARAMETERS)
                            )
                            return@patch
                        }
                        try {
                            val principal = call.principal<JWTPrincipal>()
                            val userEmail = principal!!.payload.getClaim("email").asString()
                            val user = userRepo.getUserByEmail(userEmail)!!
                            if(!userRepo.validatePassword(user,emailChangeRequest.password)){
                                call.respond(
                                    HttpStatusCode.Conflict,
                                    ErrorResponse(false,"Incorrect password")
                                )
                            }
                            if(userRepo.checkIfEmailExists(emailChangeRequest.new_email)){
                                call.respond(
                                    HttpStatusCode.Conflict,
                                    ErrorResponse(false,"Email already in use")
                                )
                            }
                            userRepo.changeEmail(userEmail, emailChangeRequest.new_email)
                            //TODO Issue new token
                            //TODO Notify user through email
                            call.respond(
                                HttpStatusCode.OK,
                                SimpleResponse(true, "Email was succesfully changed")
                            )
                        } catch (e:Exception) {
                            call.respond(
                                HttpStatusCode.InternalServerError,
                                ErrorResponse(false, e.message ?: Constants.SERVER_ERROR)
                            )
                        }
                    }
                }

            }
        }
        get("/"){
            call.respondText("no authentication needed here")
        }
    }
}

fun Application.configureAuthRouting(
    userRepo: UserRepository,
    jwtService: JwtService,
    hash: (String)->String
) {
    routing {
        route("/auth"){
            post("/login") {
                val loginRequest = try{
                    call.receive<LoginRequest>()
                } catch (e:Exception) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        SimpleResponse(false, Constants.INVALID_PARAMETERS)
                    )
                    return@post
                }
                try {
                    val (valid, error) = userRepo.validateLogin(loginRequest)
                    if(valid) {
                        val user = userRepo.getUserByEmail(loginRequest.email)
                        call.respond(
                            HttpStatusCode.OK,
                            SimpleResponse(true, jwtService.generateToken(user!!))
                        )
                    }
                    else {
                        call.respond(
                            error?.HttpStatusCode ?: HttpStatusCode.InternalServerError,
                            ErrorResponse(false,error?.Message ?: "", error?.Code ?: 0)
                        )
                    }
                } catch(e:Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ErrorResponse(false, e.message ?: Constants.SERVER_ERROR)
                    )
                }
            }
            post("/register") {
                val registerRequest = try {
                    call.receive<RegisterRequest>()
                } catch(e:Exception){
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(false, e.message ?: Constants.INVALID_PARAMETERS)
                    )
                    return@post
                }
                try {
                    val (valid, error) = userRepo.validateRegistration(registerRequest)
                    if(valid) {
                        var pictureId: String? = null
                        if (registerRequest.profilePicture != null) {
                            pictureId = UploadFile(registerRequest.profilePicture).toString()
                        }
                        // Generate Tag and check availability
                        var tag = (0..9999).random()
                        while(!userRepo.checkTagAvailability(registerRequest.username,tag)) {
                            tag = (0..9999).random()
                        }
                        val user = NewUser(
                            email = registerRequest.email,
                            username = registerRequest.username,
                            password = registerRequest.password,
                            tag = tag,
                            birthday = registerRequest.birthday,
                            gender = registerRequest.gender,
                            country = registerRequest.country,
                            premium = registerRequest.premium,
                            profilePicture = pictureId
                        )
                        val result = userRepo.addUser(user)
                        val token = jwtService.generateToken(result)
                        call.respond(
                            HttpStatusCode.OK,
                            SimpleResponse(true, token)
                        )
                    }
                    else {
                        call.respond(
                            error?.HttpStatusCode ?: HttpStatusCode.InternalServerError,
                            ErrorResponse(false,error?.Message ?: "", error?.Code ?: 0)
                        )
                    }
                } catch (e:Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ErrorResponse(false, e.message ?: "Internal Server Error")
                    )
                }
            }
        }
    }
}

private fun UploadFile(base64Image: String): UUID {
    val data = Base64.getDecoder().decode(base64Image)
    val id = UUID.randomUUID()
    val stream = FileOutputStream("c:/files/$id.jpg")
    stream.write(data)
    return id
}

private fun GetFile(fileId: String?): String? {
    if(fileId == null || fileId == ""){
        return null
    }
    val inputStream: InputStream = FileInputStream("c:/files/$fileId.jpg")

    val bytes: ByteArray
    val buffer = ByteArray(8192)
    var bytesRead: Int
    val output = ByteArrayOutputStream()

    try {
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    bytes = output.toByteArray()
    return Base64.getEncoder().encodeToString(bytes)
}