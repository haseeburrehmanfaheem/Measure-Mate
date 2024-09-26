package com.haseeb.measuremate.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.haseeb.measuremate.data.mapper.UserDto
import com.haseeb.measuremate.data.mapper.toUser
import com.haseeb.measuremate.data.util.Constants.USER_COLLECTION
import com.haseeb.measuremate.domain.model.BodyPart
import com.haseeb.measuremate.domain.model.BodyPartValue
import com.haseeb.measuremate.domain.model.User
import com.haseeb.measuremate.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DatabaseRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : DatabaseRepository {

    private fun userCollection(): CollectionReference {
        return firebaseFirestore
            .collection(USER_COLLECTION)
    }


    override fun getSignedInUser(): Flow<User?> {
        return flow {
            try {
                val userId = firebaseAuth.currentUser?.uid.orEmpty()
                userCollection()
                    .document(userId)
                    .snapshots()
                    .collect { snapshot ->
                        val userDto = snapshot.toObject(UserDto::class.java)
                        emit(userDto?.toUser())
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun addUser(): Result<Boolean> {
        return try {
            val firebaseUser = firebaseAuth.currentUser
                ?: throw IllegalArgumentException("No current user logged in.")
            var userDto = UserDto(
                userId = firebaseUser.uid,
                anonymous = firebaseUser.isAnonymous
            )
            firebaseUser.providerData.forEach { profile ->
                userDto = userDto.copy(
                    name = profile.displayName ?: userDto.name,
                    email = profile.email ?: userDto.email,
                    profilePictureUrl = profile.photoUrl?.toString() ?: userDto.profilePictureUrl
                )
            }
            userCollection()
                .document(firebaseUser.uid)
                .set(userDto)
                .await()
            Result.success(value = true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    override fun getBodyPart(bodyPartId: String): Flow<BodyPart?> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllBodyParts(): Flow<List<BodyPart>> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllBodyPartsWithLatestValue(): Flow<List<BodyPart>> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllBodyPartValues(bodyPartId: String): Flow<List<BodyPartValue>> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun upsertBodyPart(bodyPart: BodyPart): Result<Boolean> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun deleteBodyPart(bodyPartId: String): Result<Boolean> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun upsertBodyPartValue(bodyPartValue: BodyPartValue): Result<Boolean> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun deleteBodyPartValue(bodyPartValue: BodyPartValue): Result<Boolean> {
//        TODO("Not yet implemented")
//    }

}