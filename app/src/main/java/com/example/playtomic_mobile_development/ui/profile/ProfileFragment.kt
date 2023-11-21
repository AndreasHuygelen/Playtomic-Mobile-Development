package com.example.playtomic_mobile_development.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.playtomic_mobile_development.R
import com.example.playtomic_mobile_development.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var viewSwitch: ViewSwitcher
    private var isButton1Clicked = true
    private var isButton2Clicked = false

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView6: TextView = binding.username
        profileViewModel.username.observe(viewLifecycleOwner) {
            textView6.text = it
        }
        val textViewMatches: TextView = binding.matches
        profileViewModel.matches.observe(viewLifecycleOwner) {
            textViewMatches.text = it
        }
        val textViewFollowers: TextView = binding.followers
        profileViewModel.followers.observe(viewLifecycleOwner) {
            textViewFollowers.text = it
        }
        val textViewFollowed: TextView = binding.followed
        profileViewModel.followed.observe(viewLifecycleOwner) {
            textViewFollowed.text = it
        }
        val buttonEditAccount: TextView = binding.editAccount
        profileViewModel.buttonEditAccount.observe(viewLifecycleOwner) {
            buttonEditAccount.text = it
        }
        val buttonActivities: TextView = binding.activities
        profileViewModel.buttonActivities.observe(viewLifecycleOwner) {
            buttonActivities.text = it
        }
        val buttonPosts: TextView = binding.posts
        profileViewModel.buttonPosts.observe(viewLifecycleOwner) {
            buttonPosts.text = it
        }

        val textView1: TextView = binding.level
        profileViewModel.level.observe(viewLifecycleOwner) {
            textView1.text = it
        }
        val textView2: TextView = binding.test2
        profileViewModel.test2.observe(viewLifecycleOwner) {
            textView2.text = it
        }
        val textView3: TextView = binding.test3
        profileViewModel.test3.observe(viewLifecycleOwner) {
            textView3.text = it
        }
        val textView4: TextView = binding.test4
        profileViewModel.test4.observe(viewLifecycleOwner) {
            textView4.text = it
        }
        val textView5: TextView = binding.test5
        profileViewModel.test5.observe(viewLifecycleOwner) {
            textView5.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewSwitch = view.findViewById(R.id.viewSwitch)

        val buttonTest1: Button = view.findViewById(R.id.posts)
        val buttonSwitch: Button = view.findViewById(R.id.activities)

        buttonTest1.setOnClickListener {
            if (!isButton1Clicked) {
                viewSwitch.showPrevious()
                isButton1Clicked = true
                isButton2Clicked = false
            }
        }

        buttonSwitch.setOnClickListener {
            if (!isButton2Clicked) {
                viewSwitch.showNext()
                isButton2Clicked = true
                isButton1Clicked = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}