package com.example.plugins

import com.example.Constants
import com.example.data.model.*
import com.example.repository.UserRepository
import com.example.services.JwtService
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import java.io.FileOutputStream
import java.util.Base64

fun Application.configureBasicRouting() {

    // Starting point for a Ktor app:
    routing {
        authenticate("basic") {
            route("/api") {
                get("/test"){
                    call.respondText("You are authenticated!")
                }
            }
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
                        SimpleResponse(false, Constants.INVALID_PARAMETERS))
                    return@post
                }
                try {
                    val user = userRepo.findByEmail(loginRequest.email)
                    if(user != null){
                        if(user.password == hash(loginRequest.password)){
                            call.respond(
                                HttpStatusCode.OK,
                                TokenResponse(true, jwtService.generateToken(user)))
                        } else {
                            call.respond(
                                HttpStatusCode.BadRequest,
                                SimpleResponse(false, "Incorrect password"))
                        }
                    } else {
                        // TODO Probably make this happen either way if the password was incorrect
                        //      to create ambiguity for potential threats
                        call.respond(
                            HttpStatusCode.BadRequest,
                            SimpleResponse(false, "Email '${loginRequest.email}' does not exist"))
                    }
                } catch(e:Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        SimpleResponse(false, e.message ?: Constants.SERVER_ERROR)
                    )
                }
            }
            post("/register") {
                val registerRequest = try {
                    call.receive<RegisterRequest>()
                } catch(e:Exception){
                    call.respond(
                        HttpStatusCode.BadRequest,
                        SimpleResponse(false, e.message ?: Constants.INVALID_PARAMETERS))
                    return@post
                }
                try {
                    if(!userRepo.checkIfEmailExists(registerRequest.email)){
                        val user = NewUser(
                            email = registerRequest.email,
                            username = registerRequest.username,
                            password = hash(registerRequest.password))
                        if(registerRequest.profilePicture != null) { UploadFile(registerRequest.profilePicture) }
                        val result = userRepo.addUser(user)
                        val token = jwtService.generateToken(result)
                        call.respond(
                            HttpStatusCode.OK,
                            TokenResponse(true,token))
                    } else {
                        call.respond(
                            HttpStatusCode.Conflict,
                            SimpleResponse(false,"Email is already present in the database"))
                    }
                } catch (e:Exception) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        SimpleResponse(false, e.message ?: "A problem occurred"))
                }
            }
        }
    }
}

private fun UploadFile(base64Image: String) {
    val data = Base64.getDecoder().decode(base64Image)
    val stream = FileOutputStream("c:/files/image.jpg")
    stream.write(data)
}
