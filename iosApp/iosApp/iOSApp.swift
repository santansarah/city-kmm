import SwiftUI
import shared

@main
struct iOSApp: App {

    init() {
        IosAppModuleKt.doInitKoin()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
