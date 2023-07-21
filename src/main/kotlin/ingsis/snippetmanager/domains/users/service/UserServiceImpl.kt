package ingsis.snippetmanager.domains.users.service

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class UserServiceImpl: UserService {
    private val AUTH0_DOMAIN = "dev-7qnoj6g0bvw3f2qs.us.auth0.com"
    private val CLIENT_ID = "Pam5Z8KJvrA1k9bczNzgSQuWuvE1qmqe"
    private val CLIENT_SECRET = System.getenv("AUTH0_CLIENT_SECRET")
    private val API_AUDIENCE = "https://" + AUTH0_DOMAIN + "/api/v2/"


    fun getUsers(): Any {
        // Set up the RestTemplate
        val restTemplate = RestTemplate()

        // Prepare the request headers with the client credentials for authentication
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers["Authorization"] = "Bearer " + accessToken

        // Prepare the URL for the user query
        val apiUrl = API_AUDIENCE + "users"

        // Make the GET request to the Auth0 Management API to retrieve all users
        val response = restTemplate.exchange(
            apiUrl, HttpMethod.GET, HttpEntity<Any>(headers),
            String::class.java
        )

        return response.body!!
    }

    private val accessToken: String
        // Helper method to get the access token using the Client Credentials grant type
        private get() {
            val restTemplate = RestTemplate()
            val authUrl = "https://" + AUTH0_DOMAIN + "/oauth/token"
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            val requestBody =
                "{\"client_id\":\"" + CLIENT_ID + "\",\"client_secret\":\"" + CLIENT_SECRET + "\",\"audience\":\"" + API_AUDIENCE + "\",\"grant_type\":\"client_credentials\"}"
            val request = HttpEntity(requestBody, headers)
            val response = restTemplate.exchange(
                authUrl, HttpMethod.POST, request,
                String::class.java
            )

            // Parse the access token from the response body
            // (In a real application, proper error handling should be implemented here)
            return response.body!!.split("\"".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[3]
        }
    override fun findUsers(): Any {
        return getUsers()
    }
}