package com.gone.plugins

import com.example.services.JwtService
import com.gone.data.model.ErrorResponse
import com.gone.data.model.LoginRequest
import com.gone.data.model.SimpleResponse
import com.gone.repository.IdentityRepository
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting(
    identityRepository: IdentityRepository,
    jwtService: JwtService
) {
    routing {
        get("/"){
            //TODO Return status. Port status, replicated id etc
        }
        post("/login"){
            val loginRequest = try{
                call.receive<LoginRequest>()
            } catch (e:Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, e.message ?: "")
                )
                return@post
            }
            try {
                val (valid, error) = identityRepository.validateLogin(loginRequest)
                if(valid) {
                    val identity = identityRepository.getIdentity(loginRequest.email)
                    call.respond(
                        HttpStatusCode.OK,
                        SimpleResponse(true, jwtService.generateToken(identity!!))
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
                    ErrorResponse(false, e.message ?: "")
                )
            }
        }
//        post("/register"){
//            val registerRequest = try {
//                call.receive<RegisterRequest>()
//            } catch(e:Exception){
//                call.respond(
//                    HttpStatusCode.BadRequest,
//                    ErrorResponse(false, e.message ?: "")
//                )
//                return@post
//            }
//            try {
//                val (valid, error) = identityRepository.validateRegistration(registerRequest)
//                if(valid) {
//                    var pictureId: String? = null
//                    if (registerRequest.profilePicture != null) {
//                        pictureId = UploadFile(registerRequest.profilePicture).toString()
//                    }
//                    // Generate Tag and check availability
//                    var tag = (0..9999).random()
//                    while(!identityRepository.checkTagAvailability(registerRequest.username,tag)) {
//                        tag = (0..9999).random()
//                    }
//                    val user = NewUser(
//                        email = registerRequest.email,
//                        username = registerRequest.username,
//                        password = registerRequest.password,
//                        tag = tag,
//                        birthday = registerRequest.birthday,
//                        gender = registerRequest.gender,
//                        country = registerRequest.country,
//                        premium = registerRequest.premium,
//                        profilePicture = pictureId
//                    )
//                    val result = userRepo.addUser(user)
//                    val token = jwtService.generateToken(result)
//                    call.respond(
//                        HttpStatusCode.OK,
//                        SimpleResponse(true, token)
//                    )
//                }
//                else {
//                    call.respond(
//                        error?.HttpStatusCode ?: HttpStatusCode.InternalServerError,
//                        ErrorResponse(false,error?.Message ?: "", error?.Code ?: 0)
//                    )
//                }
//            } catch (e:Exception) {
//                call.respond(
//                    HttpStatusCode.InternalServerError,
//                    ErrorResponse(false, e.message ?: "Internal Server Error")
//                )
//            }
//        }
    }
}
