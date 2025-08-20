package com.example.luxorappandroid.ui.theme

/**
 * Localized strings object that provides translations for all app text
 * Matches the iOS LocalizedStrings implementation
 */
object LocalizedStrings {
    
    // Welcome and onboarding
    fun welcome(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Welcome to Luxor"
        SupportedLanguage.ARABIC -> "مرحباً بك في الأقصر"
        SupportedLanguage.FRENCH -> "Bienvenue à Louxor"
        SupportedLanguage.CHINESE -> "欢迎来到卢克索"
    }
    
    fun welcomeMessage(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Discover the ancient wonders of Egypt"
        SupportedLanguage.ARABIC -> "اكتشف عجائب مصر القديمة"
        SupportedLanguage.FRENCH -> "Découvrez les merveilles antiques de l'Égypte"
        SupportedLanguage.CHINESE -> "探索古埃及的奇迹"
    }
    
    // Authentication
    fun signIn(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Sign In"
        SupportedLanguage.ARABIC -> "تسجيل الدخول"
        SupportedLanguage.FRENCH -> "Se connecter"
        SupportedLanguage.CHINESE -> "登录"
    }
    
    fun signUp(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Sign Up"
        SupportedLanguage.ARABIC -> "إنشاء حساب"
        SupportedLanguage.FRENCH -> "S'inscrire"
        SupportedLanguage.CHINESE -> "注册"
    }
    
    fun email(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Email"
        SupportedLanguage.ARABIC -> "البريد الإلكتروني"
        SupportedLanguage.FRENCH -> "E-mail"
        SupportedLanguage.CHINESE -> "电子邮件"
    }
    
    fun password(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Password"
        SupportedLanguage.ARABIC -> "كلمة المرور"
        SupportedLanguage.FRENCH -> "Mot de passe"
        SupportedLanguage.CHINESE -> "密码"
    }
    
    // Navigation tabs
    fun places(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Places"
        SupportedLanguage.ARABIC -> "الأماكن"
        SupportedLanguage.FRENCH -> "Lieux"
        SupportedLanguage.CHINESE -> "地点"
    }

    fun chat(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Chat"
        SupportedLanguage.ARABIC -> "المحادثة"
        SupportedLanguage.FRENCH -> "Chat"
        SupportedLanguage.CHINESE -> "聊天"
    }
    
    fun termsAgreement(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "By creating an account, you agree to our"
        SupportedLanguage.ARABIC -> "بإنشاء حساب، فإنك توافق على"
        SupportedLanguage.FRENCH -> "En créant un compte, vous acceptez nos"
        SupportedLanguage.CHINESE -> "创建账户即表示您同意我们的"
    }
    
    fun termsOfService(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Terms of Service"
        SupportedLanguage.ARABIC -> "شروط الخدمة"
        SupportedLanguage.FRENCH -> "Conditions d'utilisation"
        SupportedLanguage.CHINESE -> "服务条款"
    }
    
    fun privacyPolicy(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Privacy Policy"
        SupportedLanguage.ARABIC -> "سياسة الخصوصية"
        SupportedLanguage.FRENCH -> "Politique de confidentialité"
        SupportedLanguage.CHINESE -> "隐私政策"
    }
    
    fun and(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "and"
        SupportedLanguage.ARABIC -> "و"
        SupportedLanguage.FRENCH -> "et"
        SupportedLanguage.CHINESE -> "和"
    }
    
    fun orContinueWith(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Or continue with"
        SupportedLanguage.ARABIC -> "أو تابع مع"
        SupportedLanguage.FRENCH -> "Ou continuer avec"
        SupportedLanguage.CHINESE -> "或继续使用"
    }
    
    fun back(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Back"
        SupportedLanguage.ARABIC -> "رجوع"
        SupportedLanguage.FRENCH -> "Retour"
        SupportedLanguage.CHINESE -> "返回"
    }
    
    fun createAccount(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Create Account"
        SupportedLanguage.ARABIC -> "إنشاء حساب"
        SupportedLanguage.FRENCH -> "Créer un compte"
        SupportedLanguage.CHINESE -> "创建账户"
    }
    
    fun profile(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Profile"
        SupportedLanguage.ARABIC -> "الملف الشخصي"
        SupportedLanguage.FRENCH -> "Profil"
        SupportedLanguage.CHINESE -> "个人资料"
    }
    
    fun addProfilePhoto(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Add Profile Photo"
        SupportedLanguage.ARABIC -> "إضافة صورة الملف الشخصي"
        SupportedLanguage.FRENCH -> "Ajouter une photo de profil"
        SupportedLanguage.CHINESE -> "添加头像"
    }
    
    fun camera(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Camera"
        SupportedLanguage.ARABIC -> "الكاميرا"
        SupportedLanguage.FRENCH -> "Caméra"
        SupportedLanguage.CHINESE -> "相机"
    }
    
    fun account(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Account"
        SupportedLanguage.ARABIC -> "الحساب"
        SupportedLanguage.FRENCH -> "Compte"
        SupportedLanguage.CHINESE -> "账户"
    }
    
    fun editProfile(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Edit Profile"
        SupportedLanguage.ARABIC -> "تعديل الملف الشخصي"
        SupportedLanguage.FRENCH -> "Modifier le profil"
        SupportedLanguage.CHINESE -> "编辑个人资料"
    }
    
    fun updatePersonalInfo(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Update your personal information"
        SupportedLanguage.ARABIC -> "تحديث معلوماتك الشخصية"
        SupportedLanguage.FRENCH -> "Mettre à jour vos informations personnelles"
        SupportedLanguage.CHINESE -> "更新您的个人信息"
    }
    
    fun confirmPassword(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Confirm Password"
        SupportedLanguage.ARABIC -> "تأكيد كلمة المرور"
        SupportedLanguage.FRENCH -> "Confirmer le mot de passe"
        SupportedLanguage.CHINESE -> "确认密码"
    }
    
    fun forgotPassword(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Forgot Password?"
        SupportedLanguage.ARABIC -> "نسيت كلمة المرور؟"
        SupportedLanguage.FRENCH -> "Mot de passe oublié ?"
        SupportedLanguage.CHINESE -> "忘记密码？"
    }

    // Navigation
    fun home(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Home"
        SupportedLanguage.ARABIC -> "الرئيسية"
        SupportedLanguage.FRENCH -> "Accueil"
        SupportedLanguage.CHINESE -> "首页"
    }

    fun explore(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Explore"
        SupportedLanguage.ARABIC -> "استكشف"
        SupportedLanguage.FRENCH -> "Explorer"
        SupportedLanguage.CHINESE -> "探索"
    }

    fun favorites(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Favorites"
        SupportedLanguage.ARABIC -> "المفضلة"
        SupportedLanguage.FRENCH -> "Favoris"
        SupportedLanguage.CHINESE -> "收藏"
    }

    fun settings(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Settings"
        SupportedLanguage.ARABIC -> "الإعدادات"
        SupportedLanguage.FRENCH -> "Paramètres"
        SupportedLanguage.CHINESE -> "设置"
    }
    
    // Settings
    fun language(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Language"
        SupportedLanguage.ARABIC -> "اللغة"
        SupportedLanguage.FRENCH -> "Langue"
        SupportedLanguage.CHINESE -> "语言"
    }
    
    fun theme(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Theme"
        SupportedLanguage.ARABIC -> "المظهر"
        SupportedLanguage.FRENCH -> "Thème"
        SupportedLanguage.CHINESE -> "主题"
    }
    
    fun darkMode(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Dark Mode"
        SupportedLanguage.ARABIC -> "الوضع المظلم"
        SupportedLanguage.FRENCH -> "Mode sombre"
        SupportedLanguage.CHINESE -> "深色模式"
    }
    
    fun lightMode(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Light Mode"
        SupportedLanguage.ARABIC -> "الوضع المضيء"
        SupportedLanguage.FRENCH -> "Mode clair"
        SupportedLanguage.CHINESE -> "浅色模式"
    }
    
    // Validation messages
    fun invalidEmail(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Please enter a valid email address"
        SupportedLanguage.ARABIC -> "يرجى إدخال عنوان بريد إلكتروني صحيح"
        SupportedLanguage.FRENCH -> "Veuillez saisir une adresse e-mail valide"
        SupportedLanguage.CHINESE -> "请输入有效的电子邮件地址"
    }
    
    fun passwordTooShort(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Password must be at least 8 characters"
        SupportedLanguage.ARABIC -> "يجب أن تكون كلمة المرور 8 أحرف على الأقل"
        SupportedLanguage.FRENCH -> "Le mot de passe doit contenir au moins 8 caractères"
        SupportedLanguage.CHINESE -> "密码至少需要8个字符"
    }
    
    fun passwordsDoNotMatch(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Passwords do not match"
        SupportedLanguage.ARABIC -> "كلمات المرور غير متطابقة"
        SupportedLanguage.FRENCH -> "Les mots de passe ne correspondent pas"
        SupportedLanguage.CHINESE -> "密码不匹配"
    }
    
    // General actions
    fun save(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Save"
        SupportedLanguage.ARABIC -> "حفظ"
        SupportedLanguage.FRENCH -> "Enregistrer"
        SupportedLanguage.CHINESE -> "保存"
    }
    
    fun cancel(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Cancel"
        SupportedLanguage.ARABIC -> "إلغاء"
        SupportedLanguage.FRENCH -> "Annuler"
        SupportedLanguage.CHINESE -> "取消"
    }
    
    fun done(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Done"
        SupportedLanguage.ARABIC -> "تم"
        SupportedLanguage.FRENCH -> "Terminé"
        SupportedLanguage.CHINESE -> "完成"
    }
    
    fun next(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Next"
        SupportedLanguage.ARABIC -> "التالي"
        SupportedLanguage.FRENCH -> "Suivant"
        SupportedLanguage.CHINESE -> "下一步"
    }
    
    // Language selection
    fun selectLanguage(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Select Language"
        SupportedLanguage.ARABIC -> "اختر اللغة"
        SupportedLanguage.FRENCH -> "Sélectionner la langue"
        SupportedLanguage.CHINESE -> "选择语言"
    }
    
    fun languageChanged(language: SupportedLanguage): String = when (language) {
        SupportedLanguage.ENGLISH -> "Language changed successfully"
        SupportedLanguage.ARABIC -> "تم تغيير اللغة بنجاح"
        SupportedLanguage.FRENCH -> "Langue modifiée avec succès"
        SupportedLanguage.CHINESE -> "语言更改成功"
    }
}