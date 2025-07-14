package com.arclogbook.onedrive

import android.content.Context
import com.microsoft.graph.authentication.IAuthenticationProvider
import com.microsoft.graph.requests.GraphServiceClient
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.AcquireTokenParameters
import com.microsoft.identity.client.AuthenticationCallback
import okhttp3.Request
import java.io.File
import java.util.concurrent.CompletableFuture
import java.text.SimpleDateFormat
import java.util.Date

class OneDriveSyncManager(private val context: Context) {
    private val clientId = "YOUR_CLIENT_ID" // TODO: Replace with your Azure app client ID
    private val scopes = listOf("Files.ReadWrite", "User.Read")
    private var graphClient: GraphServiceClient<Request>? = null
    private var msalApp: PublicClientApplication? = null

    fun authenticate(onSuccess: (IAuthenticationResult) -> Unit, onError: (Exception) -> Unit) {
        msalApp = PublicClientApplication(context, clientId)
        val params = AcquireTokenParameters.Builder()
            .startAuthorizationFromActivity(context as? android.app.Activity)
            .withScopes(scopes)
            .withCallback(object : AuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult) {
                    graphClient = GraphServiceClient.builder()
                        .authenticationProvider(IAuthenticationProvider { request ->
                            request.addHeader("Authorization", "Bearer ${authenticationResult.accessToken}")
                        })
                        .buildClient()
                    onSuccess(authenticationResult)
                }
                override fun onError(exception: Exception) { onError(exception) }
                override fun onCancel() { onError(Exception("Login cancelled")) }
            })
            .build()
        msalApp?.acquireToken(params)
    }

    fun uploadBackup(file: File, infoType: String, onComplete: (Boolean) -> Unit) {
        val client = graphClient ?: return onComplete(false)
        val dateStr = SimpleDateFormat("yyyy-MM-dd_HH-mm").format(Date())
        val fileName = "Logbook_${dateStr}_$infoType.json"
        CompletableFuture.runAsync {
            try {
                client.me().drive().root().itemWithPath(fileName).content().buildRequest().put(file.inputStream())
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    fun downloadBackup(destFile: File, onComplete: (Boolean) -> Unit) {
        val client = graphClient ?: return onComplete(false)
        CompletableFuture.runAsync {
            try {
                val stream = client.me().drive().root().itemWithPath("ArcLogbookBackup.json").content().buildRequest().get()
                destFile.outputStream().use { output -> stream.copyTo(output) }
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }
}
