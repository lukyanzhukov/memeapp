import ClientCommon

final class Settings {
    static let tokenSource = SettingsTokenSource(settings: AppleSettings(name: nil))
    static let loginSource = SettingsLoginSource(settings: AppleSettings(name: nil))
}
