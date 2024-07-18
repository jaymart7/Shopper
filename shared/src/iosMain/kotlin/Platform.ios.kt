import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val host: String = "localhost"
}

actual fun getPlatform(): Platform = IOSPlatform()