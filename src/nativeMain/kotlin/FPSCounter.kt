import kotlinx.cinterop.CPointer
import platform.SDL2.*

class FPSCounter(
    private val window: CPointer<SDL_Window>,
    private var lastTime: Uint32 = SDL_GetTicks(),
    private var frames: Int = 0
) {
    fun checkFPS() {
        frames++
        if (lastTime < SDL_GetTicks() - 1000u) {
            lastTime = SDL_GetTicks()
            SDL_SetWindowTitle(window, "Kotlin/native SDL app $frames FPS")
            frames = 0
        }
    }
}