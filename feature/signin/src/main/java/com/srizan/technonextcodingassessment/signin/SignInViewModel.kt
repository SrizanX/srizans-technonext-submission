package com.srizan.technonextcodingassessment.signin

import androidx.lifecycle.ViewModel
import com.srizan.technonextcodingassessment.domain.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    fun signIn(email: String, password: String) {

    }
}