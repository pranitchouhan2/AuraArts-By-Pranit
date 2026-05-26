package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {
    val artworks: Flow<List<Artwork>> = appDao.getAllArtworks()
    val inquiries: Flow<List<CommissionInquiry>> = appDao.getAllInquiries()
    val cartItems: Flow<List<CartItem>> = appDao.getCartItems()
    val notifications: Flow<List<AppNotification>> = appDao.getNotifications()
    val reels: Flow<List<ArtReel>> = appDao.getAllReels()

    fun getArtworkById(id: String): Flow<Artwork?> = appDao.getArtworkById(id)

    suspend fun insertInquiry(inquiry: CommissionInquiry) = appDao.insertInquiry(inquiry)

    suspend fun insertCartItem(item: CartItem) = appDao.insertCartItem(item)

    suspend fun updateCartItemQuantity(id: String, quantity: Int) = appDao.updateCartItemQuantity(id, quantity)

    suspend fun deleteCartItem(id: String) = appDao.deleteCartItem(id)

    suspend fun clearCart() = appDao.clearCart()

    suspend fun toggleLikeArtwork(id: String, isLiked: Boolean) = appDao.toggleLikeArtwork(id, isLiked)

    suspend fun toggleSaveArtwork(id: String, isSaved: Boolean) = appDao.toggleSaveArtwork(id, isSaved)

    suspend fun toggleLikeReel(id: String, isLiked: Boolean) = appDao.toggleLikeReel(id, isLiked)

    suspend fun insertNotification(notification: AppNotification) = appDao.insertNotification(notification)

    suspend fun markAllNotificationsRead() = appDao.markAllNotificationsRead()
}
