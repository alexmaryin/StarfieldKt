import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

class Star {
    private var x = 0f
    private var y = 0f
    private var z = 0f
    var radius = 0f
    var brightness = 0f
    var viewX = 0
    var viewY = 0

    init { newStar() }

    private fun isOffScreen() =
        z <= 0 || viewX !in -Consts.WIDTH / 2..Consts.WIDTH / 2 || viewY !in -Consts.HEIGHT / 2..Consts.HEIGHT / 2

    private fun newStar() {
        x = Random.nextInt(0..Consts.WIDTH).toFloat() - Consts.WIDTH / 2
        y = Random.nextInt(0..Consts.HEIGHT).toFloat() - Consts.HEIGHT / 2
        z = 256f
        radius = 1f
        brightness = 0f
    }

    fun process() {
        viewX = (x * 256 / z).roundToInt()
        viewY = (y * 256 / z).roundToInt()
        z -= Consts.SPEED
        radius += Consts.RADIUS_DELTA
        if (isOffScreen()) newStar()
        if (brightness < 256) brightness += 0.15f
    }
}