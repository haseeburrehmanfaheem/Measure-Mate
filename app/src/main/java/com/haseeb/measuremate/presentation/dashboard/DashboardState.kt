package com.haseeb.measuremate.presentation.dashboard

import com.haseeb.measuremate.domain.model.BodyPart
import com.haseeb.measuremate.domain.model.User

data class DashboardState(
    val user: User? = null,
    val bodyParts: List<BodyPart> = emptyList(),
    val isSignOutButtonLoading: Boolean = false,
    val isSignInButtonLoading: Boolean = false,
)