package com.example.coffeeshop

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.coffeeshop.model.CartData
import android.Manifest
import com.example.coffeeshop.model.ItemAdapter

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        createNotificationChannel(this)

        val HomeBtn: ImageButton = findViewById(R.id.HomeBtn)
        val checkOutBtn: Button = findViewById(R.id.checkOutBtn)

        checkOutBtn.setOnClickListener {
            sendOrderNotification(this)
            val intent = Intent(this, PurchaseConfirmationActivity::class.java)
            startActivity(intent)
        }
        updateTotalPrice()

        HomeBtn.setOnClickListener {
            val intent = Intent(this, MainPageActivity::class.java)
            intent.putExtra("USER", 1)
            startActivity(intent)
        }
        val CartList: ListView = findViewById(R.id.CartList)

        val itemAdapter = ItemAdapter(this, CartData.itemList, ::updateTotalPrice)
        CartList.adapter = itemAdapter

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can send notifications now
                } else {
                    // Permission denied, handle accordingly
                }
            }
        }
    }
    private fun updateTotalPrice() {
        val totalTxt: TextView = findViewById(R.id.totalTxt)
        var result = 0.0
        for (item in CartData.itemList) {
            result += item.price
        }
        val formattedNumber = String.format("%.2f", result)
        totalTxt.text = "Total: £$formattedNumber"
    }
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "order_channel",
                "Order Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for order notifications"
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendOrderNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = 1  // Unique id for the notification
        val title = "Order Confirmation"
        val message = "Your order is being processed and prepared."

        // Create an intent that will open your PurchaseConfirmationActivity when the notification is clicked
        val intent = Intent(context, PurchaseConfirmationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "order_channel")
            .setSmallIcon(R.drawable.ic_notification) // Replace with your notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Automatically remove the notification when clicked
            .build()

        notificationManager.notify(notificationId, notification)
    }
}