package com.srizan.technonextcodingassessment.signup

import androidx.lifecycle.ViewModel
import com.srizan.technonextcodingassessment.domain.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {}