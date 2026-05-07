package com.techys.domain.billing

interface BillingManager {

    suspend fun getProStatus(): Boolean
    suspend fun purchasePro(): Result<Boolean>
}