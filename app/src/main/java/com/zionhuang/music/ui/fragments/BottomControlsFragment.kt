package com.zionhuang.music.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat.STATE_NONE
import android.support.v4.media.session.PlaybackStateCompat.STATE_STOPPED
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.zionhuang.music.constants.MediaSessionConstants.ACTION_ADD_TO_LIBRARY
import com.zionhuang.music.constants.MediaSessionConstants.ACTION_TOGGLE_LIKE
import com.zionhuang.music.databinding.BottomControlsSheetBinding
import com.zionhuang.music.ui.activities.MainActivity
import com.zionhuang.music.ui.widgets.BottomSheetListener
import com.zionhuang.music.ui.widgets.MediaWidgetsController
import com.zionhuang.music.ui.widgets.PlayPauseBehavior
import com.zionhuang.music.viewmodels.PlaybackViewModel
import com.zionhuang.music.youtube.NewPipeYouTubeHelper.videoIdToUrl

class BottomControlsFragment : Fragment(), BottomSheetListener, MotionLayout.TransitionListener {
    companion object {
        private const val TAG = "BottomControlsFragment"
    }

    private lateinit var binding: BottomControlsSheetBinding
    private val viewModel by activityViewModels<PlaybackViewModel>()
    private lateinit var mediaWidgetsController: MediaWidgetsController

    private val mainActivity: MainActivity get() = requireActivity() as MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomControlsSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupUI()
    }

    private fun setupUI() {
        binding.motionLayout.background = mainActivity.binding.bottomNav.background
        viewModel.setPlayerView(binding.player)
        // Marquee
        binding.btmSongTitle.isSelected = true
        binding.btmSongArtist.isSelected = true
        binding.songTitle.isSelected = true
        binding.songArtist.isSelected = true

        binding.motionLayout.addTransitionListener(this)
        mainActivity.setBottomSheetListener(this)

        viewModel.playbackState.observe(viewLifecycleOwner) { playbackState ->
            if (playbackState.state != STATE_NONE && playbackState.state != STATE_STOPPED && !viewModel.expandOnPlay) {
                mainActivity.showBottomSheet()
            }
        }

        binding.bottomBar.setOnClickListener {
            mainActivity.expandBottomSheet()
        }

        binding.btnHide.setOnClickListener {
            mainActivity.showBottomSheet(true)
        }

        binding.btnQueue.setOnClickListener {
            mainActivity.showBottomSheet(true)
            findNavController().navigate(QueueFragmentDirections.openQueueFragment())
        }

        binding.btnAddToLibrary.setOnClickListener {
            viewModel.transportControls?.sendCustomAction(ACTION_ADD_TO_LIBRARY, null)
        }

        binding.btnFavorite.setOnClickListener {
            viewModel.transportControls?.sendCustomAction(ACTION_TOGGLE_LIKE, null)
        }

        binding.btnShare.setOnClickListener {
            viewModel.mediaData.value?.id?.let { id ->
                startActivity(Intent(Intent.ACTION_VIEW, videoIdToUrl(id)?.toUri()))
            }
        }

        with(PlayPauseBehavior(requireContext())) {
            binding.btnBtmPlayPause.setBehavior(this)
            binding.btnPlayPause.setBehavior(this)
        }

        mediaWidgetsController = MediaWidgetsController(requireContext(), binding.progressBar, binding.slider, binding.positionText)
    }

    override fun onResume() {
        super.onResume()
        viewModel.mediaController.observe(viewLifecycleOwner) {
            mediaWidgetsController.setMediaController(it)
        }
    }

    override fun onStop() {
        mediaWidgetsController.disconnectController()
        super.onStop()
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        binding.progressBar.isVisible = newState == BottomSheetBehavior.STATE_COLLAPSED
        if (newState == STATE_HIDDEN) {
            viewModel.transportControls?.stop()
        }
        binding.bottomBar.isVisible = newState != STATE_EXPANDED
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
        val progress = slideOffset.coerceIn(0f, 1f)
        binding.motionLayout.progress = progress
    }

    override fun onTransitionStarted(motionLayout: MotionLayout, i: Int, i1: Int) {
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onTransitionChange(motionLayout: MotionLayout, i: Int, i1: Int, v: Float) {}
    override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {}
    override fun onTransitionTrigger(motionLayout: MotionLayout, i: Int, b: Boolean, v: Float) {}
}