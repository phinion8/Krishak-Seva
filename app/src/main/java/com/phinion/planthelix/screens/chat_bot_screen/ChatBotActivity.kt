package com.phinion.planthelix.screens.chat_bot_screen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.phinion.planthelix.R
import com.phinion.planthelix.adapters.MessageAdapter
import com.phinion.planthelix.databinding.ActivityChatBotBinding
import com.phinion.planthelix.models.Message
import com.phinion.planthelix.models.SENT_BY_BOT
import com.phinion.planthelix.models.SENT_BY_ME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class ChatBotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBotBinding
    private lateinit var messageAdapter: MessageAdapter
    private var messageList: ArrayList<Message> = ArrayList()
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    var client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.black)

        binding.sendBtn.setOnClickListener {
            finish()
        }

        messageAdapter = MessageAdapter(messageList)
        with(binding) {
            chatRv.adapter = messageAdapter
            val layoutManager = LinearLayoutManager(this@ChatBotActivity)
            layoutManager.stackFromEnd = true
            chatRv.layoutManager = layoutManager

            sendBtn.setOnClickListener {
                val question = messageEditText.text.toString().trim()
                addToChat(question, SENT_BY_ME)
                binding.messageEditText.setText("")
                callAPI(question)
                binding.txtWelcome.visibility = View.GONE
                binding.appIcon.visibility = View.GONE
            }
        }


    }

    fun addResponse(response: String) {

        messageList.removeAt(messageList.size - 2)
        addToChat(response, SENT_BY_BOT)


    }

    private fun addToChat(message: String, sendBy: String) {
        CoroutineScope(Dispatchers.Main).launch {
            messageList.add(Message(message, sendBy))
            messageAdapter.notifyDataSetChanged()
            binding.chatRv.smoothScrollToPosition(messageAdapter.itemCount)
        }
    }

    private fun callAPI(question: String) {
        messageList.add(Message(getString(R.string.typing), SENT_BY_BOT))
        val jsonObject = JSONObject()
        try {
            jsonObject.put("model", "gpt-3.5-turbo")
            val messageArray = JSONArray()
            val obj = JSONObject()
            obj.put("role", "user")
            obj.put("content", question)
            messageArray.put(obj)
            jsonObject.put("messages", messageArray)
        } catch (e: JSONException) {
            Log.d("JSONException", e.localizedMessage)
        }

        val body = RequestBody.create(JSON, jsonObject.toString())
        val request = Request.Builder()
            .url("\thttps://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer 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")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                addResponse("Fail to load response due to " + e.localizedMessage)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    try {

                        val jsonObject = response.body?.string()?.let { JSONObject(it) }
                        var jsonArray = jsonObject?.getJSONArray("choices")
                        val result = jsonArray?.getJSONObject(0)
                            ?.getJSONObject("message")
                            ?.getString("content")

                        if (result != null) {
                            addResponse(result.trim())
                        }


                    } catch (e: JSONException) {

                    }
                } else {
                    addResponse("Fail to load response " + response.body?.string())
                }
            }

        })


    }
}