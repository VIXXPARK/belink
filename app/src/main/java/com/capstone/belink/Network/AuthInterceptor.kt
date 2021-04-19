package com.capstone.belink.Network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context):Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("x-access-token","$it")
                    .addHeader("device","android")
        }
        return chain.proceed(requestBuilder.build())
    }

    /**인터셉터를 미들웨어로 생각하면 된다.
    * 모든 retrofit서비스를 시행할 때
    * 해당 헤더를 추가해준다고 생각하면 된다.
    * 여기서 x-access-token은 jwt 토큰을 넣기위한 key,value 형식이며
    * device:android는 해당기기가 안드로이드임을 명시하기 위해 작성했다.*/

}