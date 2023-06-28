import SwiftUI
import shared

struct ContentView: View {
    let userRepo = UserPreferencesHelper().repo
    var userPreferences: UserPreferences

	var body: some View {
		
	}
    
    func getPeopleAsync() async  {
        do {
            self.userPreferences = try await asyncFunction(for: userRepo.fetchInitialPreferences())
        }
        catch {
            print("Task error: \(error)")
        }
    }
    
    mutating func test() async {
                    
                    do {
                        
                        let prefs = try await userRepo.fetchInitialPreferences()
                        
                        self.userPreferences = prefs
                        
                    } catch {
                        
                        print(error.localizedDescription)
                    }
                }
    
    
    
    func getUserPreferences() async -> UserPreferences {
        return await withCheckedContinuation{ continuation in
            userRepo.fetchInitialPreferences { data, error in
                if let prefs = data {
                    continuation.resume(returning: prefs)
                }
                if let errorReal = error {
                    continuation.resume(throwing: errorReal as! Never)
                }
            }
        }
    }
    
    
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
