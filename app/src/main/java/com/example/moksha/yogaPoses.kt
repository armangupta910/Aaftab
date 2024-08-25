package com.example.moksha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class yogaPoses : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.yoga_poses)

        findViewById<Button>(R.id.plank).setOnClickListener {
            val intent = Intent(this,demo::class.java)
            intent.putExtra("poseName","Plank")
            startActivity(intent)
        }

        findViewById<Button>(R.id.cobra).setOnClickListener {
            val intent = Intent(this,demo::class.java)
            intent.putExtra("poseName","Cobra")
            startActivity(intent)
        }

        findViewById<Button>(R.id.cobbler).setOnClickListener {
            val intent = Intent(this,demo::class.java)
            intent.putExtra("poseName","Cobbler")
            startActivity(intent)
        }

        findViewById<Button>(R.id.extended).setOnClickListener {
            val intent = Intent(this,demo::class.java)
            intent.putExtra("poseName","Extended")
            startActivity(intent)
        }

        findViewById<Button>(R.id.downward).setOnClickListener {
            val intent = Intent(this,demo::class.java)
            intent.putExtra("poseName","Downward")
            startActivity(intent)
        }

        findViewById<Button>(R.id.triangle).setOnClickListener {
            val intent = Intent(this,demo::class.java)
            intent.putExtra("poseName","Triangle")
            startActivity(intent)
        }

        findViewById<Button>(R.id.warrior1).setOnClickListener {
            val intent = Intent(this,demo::class.java)
            intent.putExtra("poseName","Warrior 1")
            startActivity(intent)
        }

        findViewById<Button>(R.id.warrior2).setOnClickListener {
            val intent = Intent(this,demo::class.java)
            intent.putExtra("poseName","Warrior 2")
            startActivity(intent)
        }

    }
}