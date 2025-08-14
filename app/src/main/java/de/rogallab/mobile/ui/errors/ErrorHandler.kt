package de.rogallab.mobile.ui.errors

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import de.rogallab.mobile.ui.base.BaseViewModel
import kotlinx.coroutines.delay

@Composable
fun ErrorHandler(
   viewModel: BaseViewModel,
   snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
   animationDuration: Long = 500L // Default animation duration
) {
   val lifecycleOwner = LocalLifecycleOwner.current

   LaunchedEffect(viewModel.errorFlow, lifecycleOwner) {
       // The lifecycle owner is used to ensure that the collection stops
       // when the composable is not in the STARTED state.
      lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
         // collect the error state and show it in a Snackbar
         viewModel.errorFlow.collect { errorState ->
            errorState?.let { it ->
               // The delay is used to allow animation to complete before Snackbar is showing the error.
               delay(animationDuration)
               showError(snackbarHostState, it)

               // Clear the error state after showing it
               viewModel.clearErrorState()
            }
         }
      }
   }
}