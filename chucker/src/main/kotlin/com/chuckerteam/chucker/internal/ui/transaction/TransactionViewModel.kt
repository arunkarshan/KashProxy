package com.chuckerteam.chucker.internal.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.appspiriment.kashproxy.ui.model.MapUrlModel
import com.appspiriment.kashproxy.utils.extentions.formatToJson
import com.chuckerteam.chucker.internal.data.entity.HttpTransaction
import com.chuckerteam.chucker.internal.data.repository.RepositoryProvider
import com.chuckerteam.chucker.internal.support.combineLatest
import org.json.JSONException

import org.json.JSONObject




internal class TransactionViewModel(transactionId: Long) : ViewModel() {

    private val mutableEncodeUrl = MutableLiveData<Boolean>(false)

    val encodeUrl: LiveData<Boolean> = mutableEncodeUrl

    val transactionTitle: LiveData<String> = RepositoryProvider.transaction()
        .getTransaction(transactionId)
        .combineLatest(encodeUrl) { transaction, encodeUrl ->
            if (transaction != null) "${transaction.method} ${transaction.getFormattedPath(encode = encodeUrl)}" else ""
        }

    val doesUrlRequireEncoding: LiveData<Boolean> = RepositoryProvider.transaction()
        .getTransaction(transactionId)
        .map { transaction ->
            if (transaction == null) {
                false
            } else {
                transaction.getFormattedPath(encode = true) != transaction.getFormattedPath(encode = false)
            }
        }

    val doesRequestBodyRequireEncoding: LiveData<Boolean> = RepositoryProvider.transaction()
        .getTransaction(transactionId)
        .map { transaction ->
            transaction?.requestContentType?.contains("x-www-form-urlencoded", ignoreCase = true)
                ?: false
        }

    val transaction: LiveData<HttpTransaction?> =
        RepositoryProvider.transaction().getTransaction(transactionId)

    val formatRequestBody: LiveData<Boolean> = doesRequestBodyRequireEncoding
        .combineLatest(encodeUrl) { requiresEncoding, encodeUrl ->
            !(requiresEncoding && encodeUrl)
        }

    fun switchUrlEncoding() = encodeUrl(!encodeUrl.value!!)

    fun encodeUrl(encode: Boolean) {
        mutableEncodeUrl.value = encode
    }

    fun getTransactionMapModel() : MapUrlModel? {
        return transaction.value?.let { transaction ->
            MapUrlModel(
                url = transaction.getFormattedUrl(encodeUrl.value?:false),
                method = transaction.method,
                response = transaction.responseBody.formatToJson(),
                isSsl = transaction.isSsl
            )
        }
    }
}

internal class TransactionViewModelFactory(
    private val transactionId: Long = 0L
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == TransactionViewModel::class.java) { "Cannot create $modelClass" }
        @Suppress("UNCHECKED_CAST")
        return TransactionViewModel(transactionId) as T
    }
}
