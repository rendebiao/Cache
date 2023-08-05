# Cache

数据缓存
    
    JsonConverter converter = new JsonConverter(){...};
    SPCache.init(this, "cache", converter);//SharedPreferences
    DBCache.init(this, "cache", new TimeProvider() {//数据库
        @Override
        public long getCurTimeMillis() {
            return System.currentTimeMillis();
        }
    }, converter);
    User writeUser = new User("张三", 30);
    SPCache.putObject("user", writeUser);
    DBCache.putObject("user", writeUser, "0", Long.MAX_VALUE);
