import Foundation

@objc public class AudioManager: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
