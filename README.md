# Cache

数据缓存
    GsonConverter converter = new GsonConverter();
    SPCache.init(this, "cache", converter);//SharedPreferences
    DBCache.init(this, "cache", new TimeProvider() {//
        @Override
        public long getCurTimeMillis() {
            return System.currentTimeMillis();
        }
    }, converter);
    User writeUser = new User("张三", 30);
    SPCache.putObject("user", writeUser);
    DBCache.putObject("user", writeUser, "0", Long.MAX_VALUE);
