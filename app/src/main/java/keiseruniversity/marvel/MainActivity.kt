package keiseruniversity.marvel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterAdapter
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button

    private val publicKey = "e3cddce7ce5912f940d17aab3539060e"
    private val privateKey = "1c4ecaf317b3106ee1c4e481e0a8c53ac83763f5"
    private val timestamp = System.currentTimeMillis().toString()
    private val hash = generateHash(timestamp, privateKey, publicKey)

    private var currentPage = 0
    private val limit = 6

    private lateinit var apiService: MarvelApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        nextButton = findViewById(R.id.nextButton)
        prevButton = findViewById(R.id.prevButton)

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = CharacterAdapter { character ->
            val intent = Intent(this, CharacterDetailActivity::class.java)
            intent.putExtra("character", character)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build()
            )
            .build()

        apiService = retrofit.create(MarvelApiService::class.java)

        loadCharacters()

        nextButton.setOnClickListener {
            currentPage++
            loadCharacters()
        }

        prevButton.setOnClickListener {
            if (currentPage > 0) {
                currentPage--
                loadCharacters()
            }
        }
    }

    private fun loadCharacters() {
        val offset = currentPage * limit
        apiService.getCharacters(publicKey, hash, timestamp, limit, offset)
            .enqueue(object : Callback<MarvelResponse> {
                override fun onResponse(call: Call<MarvelResponse>, response: Response<MarvelResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.data?.results?.let {
                            adapter.submitList(it)
                            prevButton.isEnabled = currentPage > 0
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MarvelResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun generateHash(timestamp: String, privateKey: String, publicKey: String): String {
        val input = "$timestamp$privateKey$publicKey"
        return input.md5()
    }

    private fun String.md5(): String {
        val digest = MessageDigest.getInstance("MD5")
        return digest.digest(this.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
