package com.example.moksha

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth

class getStarted : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var dotIndicator: RecyclerView
    private lateinit var dotIndicatorAdapter: DotIndicatorAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        auth = FirebaseAuth.getInstance()


        val logoutButton: ImageView = findViewById(R.id.logOut)
        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, loginPage::class.java)
            startActivity(intent)
            finish() // Finish the main activity
        }


        findViewById<ImageView>(R.id.tab1img).setOnClickListener {
            findViewById<ImageView>(R.id.tab1img).visibility = View.GONE
            findViewById<ImageView>(R.id.tab2img).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.tab3img).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.tab4img).visibility = View.VISIBLE

//            findViewById<CardView>(R.id.tab1card).visibility = View.VISIBLE
            animateCardViewToVisible(findViewById(R.id.tab1card))
            findViewById<CardView>(R.id.tab2card).visibility = View.GONE
            findViewById<CardView>(R.id.tab3card).visibility = View.GONE
            findViewById<CardView>(R.id.tab4card).visibility = View.GONE
        }

        findViewById<ImageView>(R.id.tab2img).setOnClickListener {
            findViewById<ImageView>(R.id.tab1img).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.tab2img).visibility = View.GONE
            findViewById<ImageView>(R.id.tab3img).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.tab4img).visibility = View.VISIBLE

            findViewById<CardView>(R.id.tab1card).visibility = View.GONE
//            findViewById<CardView>(R.id.tab2card).visibility = View.VISIBLE
            animateCardViewToVisible(findViewById(R.id.tab2card))
            findViewById<CardView>(R.id.tab3card).visibility = View.GONE
            findViewById<CardView>(R.id.tab4card).visibility = View.GONE
        }

        findViewById<ImageView>(R.id.tab3img).setOnClickListener {
            findViewById<ImageView>(R.id.tab1img).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.tab2img).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.tab3img).visibility = View.GONE
            findViewById<ImageView>(R.id.tab4img).visibility = View.VISIBLE

            findViewById<CardView>(R.id.tab1card).visibility = View.GONE
            findViewById<CardView>(R.id.tab2card).visibility = View.GONE
//            findViewById<CardView>(R.id.tab3card).visibility = View.VISIBLE
            animateCardViewToVisible(findViewById(R.id.tab3card))
            findViewById<CardView>(R.id.tab4card).visibility = View.GONE
        }

        findViewById<ImageView>(R.id.tab4img).setOnClickListener {
            findViewById<ImageView>(R.id.tab1img).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.tab2img).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.tab3img).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.tab4img).visibility = View.GONE

            findViewById<CardView>(R.id.tab1card).visibility = View.GONE
            findViewById<CardView>(R.id.tab2card).visibility = View.GONE
            findViewById<CardView>(R.id.tab3card).visibility = View.GONE
//            findViewById<CardView>(R.id.tab4card).visibility = View.VISIBLE
            animateCardViewToVisible(findViewById(R.id.tab4card))
        }

        viewPager = findViewById(R.id.viewPager)
        dotIndicator = findViewById(R.id.dotsIndicator)

        val carouselItems = listOf(
            CarouselItem(R.drawable.walking, "Item 1"),
            CarouselItem(R.drawable.meditation, "Item 2"),
            CarouselItem(R.drawable.stretching, "Item 3")
        )

        val adapter = CarouselAdapter(carouselItems)
        viewPager.adapter = adapter

        // Set up dot indicator
        dotIndicatorAdapter = DotIndicatorAdapter(carouselItems.size)
        dotIndicator.adapter = dotIndicatorAdapter
        dotIndicator.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Attach Page Change Listener to ViewPager2
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                dotIndicatorAdapter.setSelectedIndex(position)
            }
        })

        val space = 20 // Adjust this value to control the space between items
        viewPager.addItemDecoration(CarouselItemDecoration(space))
        viewPager.setPageTransformer(CarouselPageTransformer(1f))
    }

    private fun animateCardViewWidthToZero(cardView: CardView) {
        val initialWidth = cardView.width
        val animator = ValueAnimator.ofInt(initialWidth, 0)
        animator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = cardView.layoutParams
            layoutParams.width = animatedValue
            cardView.layoutParams = layoutParams
        }

        animator.duration = 300 // Animation duration in milliseconds
        animator.start()

        animator.doOnEnd {
            cardView.visibility = View.GONE
        }
    }

    private fun animateCardViewToVisible(cardView: CardView) {
        cardView.visibility = View.VISIBLE
        cardView.scaleX = 0f

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Float
            cardView.scaleX = animatedValue
        }

        animator.duration = 100 // Animation duration in milliseconds
        animator.start()
    }
}