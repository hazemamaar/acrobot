package com.example.acrobot.ui.fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.acrobot.R
import com.example.acrobot.data.models.Chat
import com.example.acrobot.databinding.FragmentChatBinding
import com.example.acrobot.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatFragment : Fragment() {

    private lateinit var _binding: FragmentChatBinding
    val binding get() = _binding
    var messageAdapter: MessageAdapter? = null
    lateinit var mChat: MutableList<Chat>
    private var reference: DatabaseReference? = null
    private lateinit var sharedPreferences: SharedPreferences
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        setupRecyclerView()
        binding.imageSend.setOnClickListener {
            val message=binding.message.text.toString()
            if (message.isNotEmpty()){
                sendMessage(
                    sharedPreferences.getString("name", null)!!,
                    message
                    )
                binding.message.text!!.clear()
            }

        }
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                readMessages()
            }
        })

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    private fun sendMessage(sender: String, msg: String) {

        val reference = FirebaseDatabase.getInstance().reference

        val hashMap: HashMap<String, Any> = hashMapOf()
        hashMap["sender"] = sender
        hashMap["message"] = msg

        reference.child("Chats").push().setValue(hashMap)


    }

    private fun readMessages() {
        mChat = arrayListOf()

        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mChat.clear()
                for (snapshot: DataSnapshot in dataSnapshot.children) {

                    val chat = snapshot.getValue(Chat::class.java)
                    mChat.add(chat!!)

                }
               messageAdapter?.list=mChat
                messageAdapter?.notifyItemChanged(messageAdapter!!.itemCount-1)
                binding.recycleMessage.scrollToPosition(messageAdapter!!.itemCount - 1)
            }

        })
    }
    private  fun setupRecyclerView()=binding.apply {
        this.recycleMessage.apply {
            messageAdapter = MessageAdapter(requireContext())
            adapter =messageAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        }
    }
}
