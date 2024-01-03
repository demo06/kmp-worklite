package funny.buildapp.common.ui.page

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

public abstract class BaseViewModel<T>:ViewModel() {

    public val _event: MutableSharedFlow<DispatchEvent> = MutableSharedFlow<DispatchEvent>()
    public val mainEvent: SharedFlow<DispatchEvent> = _event.asSharedFlow()

    public abstract fun dispatch(action: T)


    public fun <T> MutableSharedFlow<T>.sendEvent(event: T) {
        viewModelScope.launch {
            this@sendEvent.emit(event)
        }
    }

    public fun Any?.toast() {
        _event.sendEvent(DispatchEvent.ShowToast(this.toString()))
    }

    public fun <T> MutableStateFlow<T>.setState(reducer: T.() -> T) {
        this.value = this.value.reducer()
    }

    protected fun <T> fetchData(
        request: suspend () -> T,
        onFailed: (String) -> Unit = {},
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            request
                .asFlow()
                .flowOn(Dispatchers.Default)
                .catch {
                    onFailed("获取信息失败")
                }
                .collect {
                    if (it == null) onFailed("获取信息失败")
                    else onSuccess(it)
                }
        }
    }

}


public sealed class DispatchEvent {
   public class ShowToast(public val msg: String) : DispatchEvent()
   public data object Back : DispatchEvent()
}
