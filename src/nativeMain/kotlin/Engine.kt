import cnames.structs.SDL_Renderer
import cnames.structs.SDL_Window
import kotlinx.cinterop.*
import platform.SDL2.*

typealias EventListener = (SDL_Event) -> Unit

class Engine(width: Int, height: Int) {

    var window: CPointer<SDL_Window> private set
    private var renderer: CPointer<SDL_Renderer>
    private val eventListeners = mutableListOf<EventListener>()
    private var isRunning = false

    init {
        if (SDL_Init(SDL_INIT_EVERYTHING) != 0) {
            println("SDL2 doesn't initialized! ${SDL_GetError()}")
            SDL_Quit()
        }

        window = SDL_CreateWindow(
            "Kotlin/native SDL app",
            SDL_WINDOWPOS_CENTERED.toInt(), SDL_WINDOWPOS_CENTERED.toInt(),
            width, height, SDL_WINDOW_ALLOW_HIGHDPI
        ) ?: throw RuntimeException("SDL2 can't create window! ${SDL_GetError()}")

        renderer = SDL_CreateRenderer(window, -1, 0)
            ?: throw RuntimeException("SDL2 can't create render! ${SDL_GetError()}")
    }

    fun addEventListener(name: String = "undefined", block: EventListener) {
        eventListeners += block
        println("Added event listener $name")
    }

    fun startInfiniteLoop(block: () -> Unit) {
        isRunning = true
        memScoped {
            val windowEvent = alloc<SDL_Event>()
            while (isRunning) {
                if (SDL_PollEvent(windowEvent.ptr.reinterpret()) != 0) {
                    eventListeners.forEach { it(windowEvent) }
                }
                block()
            }
        }
    }

    fun onEachFrame(block: CPointer<SDL_Renderer>.() -> Unit) {
        SDL_SetRenderDrawColor(renderer, 0, 0, 0, 255)
        SDL_RenderClear(renderer)
        block(renderer)
        SDL_RenderPresent(renderer)
    }

    private fun closeEngine() {
        println("Closing SDL engine")
        eventListeners.clear()
        SDL_DestroyRenderer(renderer)
        SDL_DestroyWindow(window)
        SDL_Quit()
    }

    fun stopLoop() {
        isRunning = false
        closeEngine()
    }
}

fun SDLEngine(width: Int, height: Int, body: Engine.() -> Unit) {
    val engine = Engine(width, height)
    body(engine)
}
