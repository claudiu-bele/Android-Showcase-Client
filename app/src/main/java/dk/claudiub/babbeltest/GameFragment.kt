package dk.claudiub.babbeltest

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import dk.claudiub.babbeltest.api_impl.GameViewModelImpl
import dk.claudiub.babbeltest.core.AsyncResource
import dk.claudiub.babbeltest.databinding.FragmentGameBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class GameFragment : Fragment() {

    lateinit var binding: FragmentGameBinding

    val gameVm :GameViewModelImpl by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        binding.buttonRight.setOnClickListener {
            gameVm.markAsRight()
        }
        binding.buttonWrong.setOnClickListener {
            gameVm.markAsWrong()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameVm.scoreLiveData.observe(viewLifecycleOwner, Observer {
            val score = it ?: 0
            binding.score.text = requireContext().getString(R.string.game__score, score)
        })
        val displayMetrics = DisplayMetrics()
        gameVm.cardPositionLiveData.observe(viewLifecycleOwner, Observer {
            val pos = it
            val constraintSet = ConstraintSet()

            constraintSet.clone(binding.root)
            constraintSet.setVerticalBias(R.id.translation_card, pos)
            constraintSet.applyTo(binding.root)
        })
        gameVm.currentCardLiveData.observe(viewLifecycleOwner, Observer {
            val isLoading = it?.status == AsyncResource.Status.LOADING
            binding.progress.isVisible = isLoading
            when(it?.status) {
                AsyncResource.Status.SUCCESS -> {
                    binding.firstTranslation.text = it.data?.englishText
                    binding.lastTranslation.text = it.data?.translatedText
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }
}