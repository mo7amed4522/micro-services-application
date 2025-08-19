import SwiftUI
import Foundation

// MARK: - Language Manager
class LanguageManager: ObservableObject {
    @Published var currentLanguage: SupportedLanguage = .english {
        didSet {
            UserDefaults.standard.set(currentLanguage.rawValue, forKey: "selectedLanguage")
        }
    }
    
    init() {
        if let savedLanguage = UserDefaults.standard.string(forKey: "selectedLanguage"),
           let language = SupportedLanguage(rawValue: savedLanguage) {
            self.currentLanguage = language
        } else {
            let systemLanguage = Locale.current.language.languageCode?.identifier ?? "en"
            switch systemLanguage {
            case "ar":
                self.currentLanguage = .arabic
            case "fr":
                self.currentLanguage = .french
            case "zh":
                self.currentLanguage = .chinese
            default:
                self.currentLanguage = .english
            }
        }
    }
    
    func changeLanguage(to language: SupportedLanguage) {
        currentLanguage = language
    }
}

// MARK: - Supported Languages
enum SupportedLanguage: String, CaseIterable {
    case english = "en"
    case arabic = "ar"
    case french = "fr"
    case chinese = "zh"
    
    var displayName: String {
        switch self {
        case .english: return "English"
        case .arabic: return "العربية"
        case .french: return "Français"
        case .chinese: return "中文"
        }
    }
    
    var flag: String {
        switch self {
        case .english: return "🇺🇸"
        case .arabic: return "🇦🇪"
        case .french: return "🇫🇷"
        case .chinese: return "🇨🇳"
        }
    }
    
    var isRTL: Bool {
        return self == .arabic
    }
}

// MARK: - Localized Strings
struct LocalizedStrings {
    
    // MARK: - Welcome & Onboarding
    static func welcomeTitle(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Welcome to Luxor"
        case .arabic: return "مرحباً بك في الأقصر"
        case .french: return "Bienvenue à Louxor"
        case .chinese: return "欢迎来到卢克索"
        }
    }
    
    static func welcomeSubtitle(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Journey Through Time"
        case .arabic: return "رحلة عبر الزمن"
        case .french: return "Voyage à travers le temps"
        case .chinese: return "穿越时空之旅"
        }
    }
    
    static func welcomeDescription(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Discover the ancient wonders and timeless beauty of Luxor, where history comes alive."
        case .arabic: return "اكتشف عجائب الأقصر القديمة وجمالها الخالد، حيث يحيا التاريخ."
        case .french: return "Découvrez les merveilles antiques et la beauté intemporelle de Louxor, où l'histoire prend vie."
        case .chinese: return "探索卢克索的古代奇迹和永恒之美，在这里历史重现生机。"
        }
    }
    
    static func next(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Next"
        case .arabic: return "التالي"
        case .french: return "Suivant"
        case .chinese: return "下一步"
        }
    }
    
    static func skip(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Skip"
        case .arabic: return "تخطي"
        case .french: return "Passer"
        case .chinese: return "跳过"
        }
    }
    
    static func getStarted(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Get Started"
        case .arabic: return "ابدأ الآن"
        case .french: return "Commencer"
        case .chinese: return "开始使用"
        }
    }
    
    // MARK: - Authentication
    static func login(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Login"
        case .arabic: return "تسجيل الدخول"
        case .french: return "Connexion"
        case .chinese: return "登录"
        }
    }
    
    static func register(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Register"
        case .arabic: return "إنشاء حساب"
        case .french: return "S'inscrire"
        case .chinese: return "注册"
        }
    }
    
    static func email(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Email"
        case .arabic: return "البريد الإلكتروني"
        case .french: return "E-mail"
        case .chinese: return "邮箱"
        }
    }
    
    static func password(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Password"
        case .arabic: return "كلمة المرور"
        case .french: return "Mot de passe"
        case .chinese: return "密码"
        }
    }
    
    static func fullName(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Full Name"
        case .arabic: return "الاسم الكامل"
        case .french: return "Nom complet"
        case .chinese: return "全名"
        }
    }
    
    static func phoneNumber(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Phone Number"
        case .arabic: return "رقم الهاتف"
        case .french: return "Numéro de téléphone"
        case .chinese: return "电话号码"
        }
    }
    
    static func forgotPassword(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Forgot Password?"
        case .arabic: return "نسيت كلمة المرور؟"
        case .french: return "Mot de passe oublié ?"
        case .chinese: return "忘记密码？"
        }
    }
    
    // MARK: - Navigation
    static func home(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Home"
        case .arabic: return "الرئيسية"
        case .french: return "Accueil"
        case .chinese: return "首页"
        }
    }
    
    static func places(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Places"
        case .arabic: return "الأماكن"
        case .french: return "Lieux"
        case .chinese: return "地点"
        }
    }
    
    static func chat(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Chat"
        case .arabic: return "المحادثة"
        case .french: return "Chat"
        case .chinese: return "聊天"
        }
    }
    
    static func settings(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Settings"
        case .arabic: return "الإعدادات"
        case .french: return "Paramètres"
        case .chinese: return "设置"
        }
    }
    
    // MARK: - Search & Discovery
    static func searchPlaces(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Search amazing places..."
        case .arabic: return "ابحث عن أماكن رائعة..."
        case .french: return "Rechercher des lieux incroyables..."
        case .chinese: return "搜索精彩地点..."
        }
    }
    
    static func discover(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Discover"
        case .arabic: return "اكتشف"
        case .french: return "Découvrir"
        case .chinese: return "发现"
        }
    }
    
    static func explore(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Explore"
        case .arabic: return "استكشف"
        case .french: return "Explorer"
        case .chinese: return "探索"
        }
    }
    
    // MARK: - Categories
    static func all(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "All"
        case .arabic: return "الكل"
        case .french: return "Tout"
        case .chinese: return "全部"
        }
    }
    
    static func temples(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Temples"
        case .arabic: return "المعابد"
        case .french: return "Temples"
        case .chinese: return "神庙"
        }
    }
    
    static func tombs(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Tombs"
        case .arabic: return "المقابر"
        case .french: return "Tombeaux"
        case .chinese: return "陵墓"
        }
    }
    
    static func museums(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Museums"
        case .arabic: return "المتاحف"
        case .french: return "Musées"
        case .chinese: return "博物馆"
        }
    }
    
    // MARK: - Settings
    static func language(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Language"
        case .arabic: return "اللغة"
        case .french: return "Langue"
        case .chinese: return "语言"
        }
    }
    
    static func darkMode(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Dark Mode"
        case .arabic: return "الوضع المظلم"
        case .french: return "Mode sombre"
        case .chinese: return "深色模式"
        }
    }
    
    static func notifications(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Notifications"
        case .arabic: return "الإشعارات"
        case .french: return "Notifications"
        case .chinese: return "通知"
        }
    }
    
    static func logout(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Logout"
        case .arabic: return "تسجيل الخروج"
        case .french: return "Déconnexion"
        case .chinese: return "退出登录"
        }
    }
    
    // MARK: - Validation Messages
    static func invalidEmail(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Please enter a valid email address"
        case .arabic: return "يرجى إدخال عنوان بريد إلكتروني صحيح"
        case .french: return "Veuillez saisir une adresse e-mail valide"
        case .chinese: return "请输入有效的邮箱地址"
        }
    }
    
    static func passwordTooShort(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Password must be at least 8 characters"
        case .arabic: return "يجب أن تكون كلمة المرور 8 أحرف على الأقل"
        case .french: return "Le mot de passe doit contenir au moins 8 caractères"
        case .chinese: return "密码至少需要8个字符"
        }
    }
    
    static func invalidPhoneNumber(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Please enter a valid phone number"
        case .arabic: return "يرجى إدخال رقم هاتف صحيح"
        case .french: return "Veuillez saisir un numéro de téléphone valide"
        case .chinese: return "请输入有效的电话号码"
        }
    }
}

// MARK: - Localization Helper Extension
extension View {
    func localizedEnvironment(_ languageManager: LanguageManager) -> some View {
        self.environment(\.layoutDirection, languageManager.currentLanguage.isRTL ? .rightToLeft : .leftToRight)
    }
}

// MARK: - String Extension for Easy Access
extension String {
    func localized(_ language: SupportedLanguage) -> String {
        // This can be extended to use actual .strings files if needed
        return self
    }
}
