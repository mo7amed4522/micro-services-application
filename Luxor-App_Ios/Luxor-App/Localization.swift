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
    static func authenticating(_ language: SupportedLanguage) -> String {
    switch language {
    case .english: return "Authenticating..."
    case .arabic: return "جارٍ التحقق..."
    case .french: return "Authentification..."
    case .chinese: return "正在验证..."
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
    static func title1(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Discover Ancient Wonders"
            case .arabic: return "اكتشف العجائب القديمة"
            case .french: return "Découvrez les merveilles anciennes"
            case .chinese: return "探索古代奇观"
            }
        }
    static func CreateAccount(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Create Account"
        case .arabic: return "إنشاء حساب"
        case .french: return "Créer un compte"
        case .chinese: return "创建账户"
        }
    }
    static func AddProfilePhoto(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Add Profile Photo"
        case .arabic: return "أضف صورة الملف الشخصي"
        case .french: return "Ajouter une photo de profil"
        case .chinese: return "添加头像"
        }
    }
    static func SignInToYourAccount(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Sign in to your account"
        case .arabic: return "سجّل الدخول إلى حسابك"
        case .french: return "Connectez-vous à votre compte"
        case .chinese: return "登录到您的账户"
        }
    }

    static func SignIn(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Sign In"
        case .arabic: return "تسجيل الدخول"
        case .french: return "Se connecter"
        case .chinese: return "登录"
        }
    }

    static func OR(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "OR"
        case .arabic: return "أو"
        case .french: return "OU"
        case .chinese: return "或"
        }
    }
    static func PasswordRequirements(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Password Requirements"
        case .arabic: return "متطلبات كلمة المرور"
        case .french: return "Exigences du mot de passe"
        case .chinese: return "密码要求"
        }
    }
    static func AtLeastOneSpecialCharacter(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "At least one special character"
        case .arabic: return "حرف خاص واحد على الأقل"
        case .french: return "Au moins un caractère spécial"
        case .chinese: return "至少包含一个特殊字符"
        }
    }
    static func AtLeastEightCharacters(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "At least 8 characters"
        case .arabic: return "ثمانية أحرف على الأقل"
        case .french: return "Au moins 8 caractères"
        case .chinese: return "至少8个字符"
        }
    }
    static func AtLeastOneCapitalLetter(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "At least one capital letter"
        case .arabic: return "حرف كبير واحد على الأقل"
        case .french: return "Au moins une lettre majuscule"
        case .chinese: return "至少包含一个大写字母"
        }
    }
    static func FullNameValidationMessage(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Please enter your full name (at least 2 words)"
        case .arabic: return "يرجى إدخال اسمك الكامل (كلمتان على الأقل)"
        case .french: return "Veuillez entrer votre nom complet (au moins 2 mots)"
        case .chinese: return "请输入您的全名（至少两个单词）"
        }
    }
    static func EmailAddress(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Email Address"
        case .arabic: return "البريد الإلكتروني"
        case .french: return "Adresse e-mail"
        case .chinese: return "电子邮件地址"
        }
    }
    static func InvalidEmailMessage(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Please enter a valid email address"
        case .arabic: return "يرجى إدخال بريد إلكتروني صالح"
        case .french: return "Veuillez saisir une adresse e-mail valide"
        case .chinese: return "请输入有效的电子邮件地址"
        }
    }
    static func ByCreatingAccount(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "By creating an account, you agree to our"
        case .arabic: return "بإنشاء حساب، فإنك توافق على"
        case .french: return "En créant un compte, vous acceptez nos"
        case .chinese: return "创建账户即表示您同意我们的"
        }
    }
    static func And(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "and"
        case .arabic: return "و"
        case .french: return "et"
        case .chinese: return "和"
        }
    }
    static func subtitle1(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Journey Through History"
            case .arabic: return "رحلة عبر التاريخ"
            case .french: return "Voyage à travers l'histoire"
            case .chinese: return "穿越历史的旅程"
            }
        }
        static func description1(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Explore the magnificent temples and tombs of ancient Egypt with our immersive historical experience."
            case .arabic: return "استكشف المعابد والمقابر الرائعة لمصر القديمة من خلال تجربتنا التاريخية الغامرة."
            case .french: return "Explorez les magnifiques temples et tombes de l'Égypte ancienne grâce à notre expérience historique immersive."
            case .chinese: return "通过我们的沉浸式历史体验，探索古埃及宏伟的神庙和墓葬。"
            }
        }
    static func title2(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Uncover Hidden Treasures"
            case .arabic: return "اكشف الكنوز المخفية"
            case .french: return "Découvrez des trésors cachés"
            case .chinese: return "发现隐藏的宝藏"
            }
        }
        static func subtitle2(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Archaeological Adventures"
            case .arabic: return "مغامرات أثرية"
            case .french: return "Aventures archéologiques"
            case .chinese: return "考古探险"
            }
        }
        static func description2(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Join archaeological expeditions and discover artifacts that tell the story of ancient civilizations."
            case .arabic: return "انضم إلى بعثات أثرية واكتشف القطع الأثرية التي تروي قصة الحضارات القديمة."
            case .french: return "Participez à des expéditions archéologiques et découvrez des artefacts racontant l'histoire des civilisations anciennes."
            case .chinese: return "加入考古探险，发现讲述古代文明故事的文物。"
            }
        }

        // MARK: - Page 3
        static func title3(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Walk Among Pharaohs"
            case .arabic: return "امشِ بين الفراعنة"
            case .french: return "Marchez parmi les pharaons"
            case .chinese: return "与法老同行"
            }
        }
        static func subtitle3(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Royal Experiences"
            case .arabic: return "تجارب ملكية"
            case .french: return "Expériences royales"
            case .chinese: return "皇家体验"
            }
        }
        static func description3(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Experience the grandeur of pharaohs and learn about the powerful rulers of ancient Egypt."
            case .arabic: return "اختبر عظمة الفراعنة وتعرف على الحكام الأقوياء لمصر القديمة."
            case .french: return "Vivez la grandeur des pharaons et découvrez les puissants dirigeants de l'Égypte ancienne."
            case .chinese: return "体验法老的宏伟，并了解古埃及的强大统治者。"
            }
        }

        // MARK: - Page 4
        static func title4(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Decode Ancient Mysteries"
            case .arabic: return "فك الشفرات القديمة"
            case .french: return "Décodez les mystères anciens"
            case .chinese: return "破解古代谜团"
            }
        }
        static func subtitle4(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Hieroglyphic Secrets"
            case .arabic: return "أسرار الهيروغليفية"
            case .french: return "Secrets hiéroglyphiques"
            case .chinese: return "象形文字的秘密"
            }
        }
        static func description4(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Learn to read hieroglyphs and unlock the secrets written in stone thousands of years ago."
            case .arabic: return "تعلم قراءة الهيروغليفية وافتح الأسرار المنقوشة على الحجر منذ آلاف السنين."
            case .french: return "Apprenez à lire les hiéroglyphes et découvrez les secrets gravés dans la pierre il y a des milliers d'années."
            case .chinese: return "学习阅读象形文字，解锁数千年前刻在石头上的秘密。"
            }
        }

        // MARK: - Page 5
        static func title5(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Sacred Rituals"
            case .arabic: return "الطقوس المقدسة"
            case .french: return "Rituels sacrés"
            case .chinese: return "神圣仪式"
            }
        }
        static func subtitle5(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Spiritual Journey"
            case .arabic: return "رحلة روحية"
            case .french: return "Voyage spirituel"
            case .chinese: return "心灵之旅"
            }
        }
        static func description5(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Understand the spiritual beliefs and sacred rituals that guided ancient Egyptian life."
            case .arabic: return "افهم المعتقدات الروحية والطقوس المقدسة التي أرشدت حياة المصريين القدماء."
            case .french: return "Comprenez les croyances spirituelles et les rituels sacrés qui guidaient la vie dans l'Égypte ancienne."
            case .chinese: return "了解指导古埃及生活的精神信仰和神圣仪式。"
            }
        }

        // MARK: - Page 6
        static func title6(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Begin Your Adventure"
            case .arabic: return "ابدأ مغامرتك"
            case .french: return "Commencez votre aventure"
            case .chinese: return "开始您的冒险"
            }
        }
        static func subtitle6(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Your Journey Awaits"
            case .arabic: return "رحلتك بانتظارك"
            case .french: return "Votre voyage vous attend"
            case .chinese: return "您的旅程已准备好"
            }
        }
        static func description6(_ language: SupportedLanguage) -> String {
            switch language {
            case .english: return "Ready to embark on an unforgettable journey through the wonders of ancient Egypt?"
            case .arabic: return "هل أنت مستعد لبدء رحلة لا تُنسى عبر عجائب مصر القديمة؟"
            case .french: return "Prêt à entreprendre un voyage inoubliable à travers les merveilles de l'Égypte ancienne ?"
            case .chinese: return "准备好开始一段难忘的古埃及奇观之旅吗？"
            }
        }
    static func Skip(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Skip"
        case .arabic: return "تخطي"
        case .french: return "Passer"
        case .chinese: return  "跳过"
        }
    }
    static func GetStart(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Get Started"
        case .arabic: return "ابدأ المغامرة"
        case .french: return "Commencer"
        case .chinese: return "开始"
        }
    }

    static func Next(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Next"
        case .arabic: return "التالي"
        case .french: return "Suivant"
        case .chinese: return "下一步"
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
    static func SubTitlelanguage(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Choose your preferred language"
        case .arabic: return "اختر لغتك المفضلة"
        case .french: return "Choisissez votre langue préférée"
        case .chinese: return "选择您偏好的语言"
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
    static func SubTitledarkMode(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Switch between light and dark themes"
        case .arabic: return "التبديل بين الوضع الفاتح والوضع المظلم"
        case .french: return "Basculer entre les thèmes clair et sombre"
        case .chinese: return "在浅色和深色主题之间切换"
        }
    }
    static func EditProfile(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Edit Profile"
        case .arabic: return "تعديل الملف الشخصي"
        case .french: return "Modifier le profil"
        case .chinese: return "编辑资料"
        }
    }
    static func SubtitleEditProfile(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Update your personal information"
        case .arabic: return "تحديث معلوماتك الشخصية"
        case .french: return "Mettez à jour vos informations personnelles"
        case .chinese: return "更新您的个人信息"
        }
    }
    static func Privacy(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Privacy"
        case .arabic: return "الخصوصية"
        case .french: return "Confidentialité"
        case .chinese: return "隐私"
        }
    }
    static func SubTitlePrivacy(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Control your privacy settings"
        case .arabic: return "تحكم في إعدادات الخصوصية الخاصة بك"
        case .french: return "Contrôlez vos paramètres de confidentialité"
        case .chinese: return "管理您的隐私设置"
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
    static func SubTitleNotifications(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Manage your notification preferences"
        case .arabic: return "إدارة تفضيلات الإشعارات الخاصة بك"
        case .french: return "Gérez vos préférences de notification"
        case .chinese: return "管理您的通知偏好"
        }
    }
    static func PaymentMethod(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Payment Methods"
        case .arabic: return "طرق الدفع"
        case .french: return "Méthodes de paiement"
        case .chinese: return "支付方式"
        }
    }
    static func SubTitlePaymentMethod(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Manage your payment options"
        case .arabic: return  "إدارة خيارات الدفع الخاصة بك"
        case .french: return "Gérez vos options de paiement"
        case .chinese: return "管理您的付款选项"
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
    
    static func Preferences(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Preferences"
        case .arabic: return "الإعدادات"
        case .french: return "Préférences"
        case .chinese: return "首选项"
        }
    }
    static func LocationServices(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Location Services"
        case .arabic: return "خدمات الموقع"
        case .french: return "Services de localisation"
        case .chinese: return "位置服务"
        }
    }
    static func SubTitleLocationServices(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Allow access to your location"
        case .arabic: return  "السماح بالوصول إلى موقعك"
        case .french: return "Autoriser l'accès à votre position"
        case .chinese: return "允许访问您的位置"
        }
    }
    static func BiometricLogin(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Biometric Login"
        case .arabic: return "تسجيل الدخول باستخدام القياسات الحيوية"
        case .french: return "Connexion biométrique"
        case .chinese: return "生物识别登录"
        }
    }
    static func SubTitleBiometricLogin(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Use Face ID or Touch ID to sign in"
        case .arabic: return "استخدم بصمة الوجه أو بصمة الإصبع لتسجيل الدخول"
        case .french: return "Utilisez Face ID ou Touch ID pour vous connecter"
        case .chinese: return "使用面部识别或指纹登录"
        }
    }
    static func Currency(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Currency"
        case .arabic: return "العملة"
        case .french: return "Devise"
        case .chinese: return "货币"
        }
    }
    static func SubTitleCurrency(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Select your preferred currency"
        case .arabic: return "اختر العملة المفضلة لديك"
        case .french: return "Sélectionnez votre devise préférée"
        case .chinese: return "选择您偏好的货币"
        }
    }
    static func PrivacySecurity(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Privacy & Security"
        case .arabic: return "الخصوصية والأمان"
        case .french: return "Confidentialité et sécurité"
        case .chinese: return "隐私与安全"
        }
    }
    static func TwoFactorAuthentication(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Two-Factor Authentication"
        case .arabic: return "المصادقة الثنائية"
        case .french: return "Authentification à deux facteurs"
        case .chinese: return "双重认证"
        }
    }
    static func SubtitleTwoFactorAuthentication(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Add an extra layer of security"
        case .arabic: return "أضف طبقة إضافية من الأمان"
        case .french: return "Ajoutez une couche de sécurité supplémentaire"
        case .chinese: return "添加额外的安全层"
        }
    }
    static func ChangePassword(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Change Password"
        case .arabic: return "تغيير كلمة المرور"
        case .french: return "Changer le mot de passe"
        case .chinese: return "更改密码"
        }
    }
    static func SubTitleChangePassword(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return  "Update your account password"
        case .arabic: return "قم بتحديث كلمة مرور حسابك"
        case .french: return "Mettez à jour le mot de passe de votre compte"
        case .chinese: return "更新您的账户密码"
        }
    }
    static func AccountPrivacy(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Account Privacy"
        case .arabic: return "خصوصية الحساب"
        case .french: return "Confidentialité du compte"
        case .chinese: return "账户隐私"
        }
    }
    static func SubTitleAccountPrivacy(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return  "Control who can see your profile"
        case .arabic: return "تحكم بمن يمكنه رؤية ملفك الشخصي"
        case .french: return "Contrôlez qui peut voir votre profil"
        case .chinese: return "控制谁可以查看您的个人资料"
        }
    }
    static func DeleteAccount(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Delete Account"
        case .arabic: return "حذف الحساب"
        case .french: return "Supprimer le compte"
        case .chinese: return "删除账户"
        }
    }
    static func SubTitleDeleteAccount(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return  "Permanently delete your account"
        case .arabic: return "حذف حسابك بشكل دائم"
        case .french: return "Supprimez définitivement votre compte"
        case .chinese: return "永久删除您的账户"
        }
    }
     static func AppSeetting(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "App Settings"
        case .arabic: return "إعدادات التطبيق"
        case .french: return "Paramètres de l'application"
        case .chinese: return "应用设置"
        }
    }
    static func AutoPlayVideos(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Auto-play Videos"
        case .arabic: return "تشغيل الفيديو تلقائيًا"
        case .french: return "Lecture automatique des vidéos"
        case .chinese: return "自动播放视频"
        }
    }
    static func SubTitleAutoPlayVideos(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Automatically play videos in feed"
        case .arabic: return "تشغيل الفيديوهات تلقائيًا في الخلاصة"
        case .french: return "Lire automatiquement les vidéos dans le fil"
        case .chinese: return "在动态中自动播放视频"
        }
    }
    static func DataSaver(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Data Saver"
        case .arabic: return "توفير البيانات"
        case .french: return "Économie de données"
        case .chinese: return"数据节省"
        }
    }
    static func SubTitleDataSaver(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Reduce data usage"
        case .arabic: return "تقليل استهلاك البيانات"
        case .french: return "Réduire la consommation de données"
        case .chinese: return "减少数据使用"
        }
    }
    static func CheckForUpdate(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Check for Updates"
        case .arabic: return "التحقق من التحديثات"
        case .french: return "Vérifier les mises à jour"
        case .chinese: return "检查更新"
        }
    }
    static func SubTitleCheckForUpdate(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Current version: 1.0.0"
        case .arabic: return  "الإصدار الحالي: 1.0.0"
        case .french: return "Version actuelle : 1.0.0"
        case .chinese: return "当前版本：1.0.0"
        }
    }
    static func TermsOfServices(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "TermsOfServices"
        case .arabic: return "شروط الخدمة"
        case .french: return "Conditions d'utilisation"
        case .chinese: return "服务条款"
        }
    }
    static func SubTitleTermsOfServices(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Read our terms and conditions"
        case .arabic: return "اقرأ شروطنا وأحكامنا"
        case .french: return "Lisez nos termes et conditions"
        case .chinese: return "阅读我们的条款和条件"
        }
    }
    static func PrivacyAndPolicy(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Privacy Policy"
        case .arabic: return "سياسة الخصوصية"
        case .french: return "Politique de confidentialité"
        case .chinese: return "隐私政策"
        }
    }
    static func SubTitlePrivacyAndPolicy(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Learn about data collection"
        case .arabic: return "تعرف على جمع البيانات"
        case .french: return "En savoir plus sur la collecte de données"
        case .chinese: return "了解数据收集情况"
        }
    }
    static func HelpCenter(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Contact Support"
        case .arabic: return "مركز المساعدة"
        case .french: return "Centre d'assistance"
        case .chinese: return "帮助中心"
        }
    }
    static func SubTitleHelpCenter(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Reach out to our support team"
        case .arabic: return "تواصل مع فريق الدعم لدينا"
        case .french: return "Contactez notre équipe d'assistance"
        case .chinese: return "联系支持团队"
        }
    }
    static func RateApp(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Rate App"
        case .arabic: return "تقييم التطبيق"
        case .french: return "Évaluer l'application"
        case .chinese: return "应用评分"
        }
    }
    static func SubTitleRateApp(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Share your feedback"
        case .arabic: return  "شارك رأيك"
        case .french: return "Partagez vos commentaires"
        case .chinese: return "分享您的反馈"
        }
    }
    static func ShareApp(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Share App"
        case .arabic: return "مشاركة التطبيق"
        case .french: return "Partager l'application"
        case .chinese: return "分享应用"
        }
    }
    static func SubTitleShareApp(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Tell friends about Luxor"
        case .arabic: return "أخبر أصدقائك عن الاقصر"
        case .french: return  "Parlez de Luxor à vos amis"
        case .chinese: return "告诉朋友关于 Luxor"
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
    static func Support(_ language: SupportedLanguage) -> String {
        switch language {
        case .english: return "Support"
        case .arabic: return "الدعم"
        case .french: return "Support"
        case .chinese: return "支持"
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
