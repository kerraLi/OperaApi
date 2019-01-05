package com.ywxt.Enum;

// ali 区域region
public enum AliRegion {
    QINGDAO(0, "cn-qingdao", "cn", "华北1", "青岛"),
    BEIJING(1, "cn-beijing", "cn", "华北2", "北京"),
    ZHANGJIAKOU(2, "cn-zhangjiakou", "cn", "华北3", "张家口"),
    HUHEHAOTE(3, "cn-huhehaote", "cn", "华北5", "呼和浩特"),
    HANGZHOU(4, "cn-hangzhou", "cn", "华东1", "杭州"),
    SHANGHAI(5, "cn-shanghai", "cn", "华东2", "上海"),
    SHENZHEN(6, "cn-shenzhen", "cn", "华南1", "深圳"),
    HONGKONG(7, "cn-hongkong", "cn", "香港", "香港"),
    APSE1(8, "ap-southeast-1", "ap", "亚太东南1", "新加坡"),
    APSE2(9, "ap-southeast-2", "ap", "亚太东南2", "悉尼"),
    APSE3(10, "ap-southeast-3", "ap", "亚太东南3", "吉隆坡"),
    APSE5(11, "ap-southeast-5", "ap", "亚太东南5", "雅加达"),
    APS1(12, "ap-south-1", "ap", "亚太南部1", "孟买"),
    APNE1(13, "ap-northeast-1", "ap", "亚太东北1", "东京"),
    USW1(14, "us-west-1", "us", "美国西部1", "硅谷"),
    USE1(15, "us-east-1", "us", "美国东部1", "弗吉尼亚"),
    EUC1(16, "eu-central-1", "eu", "欧洲中部1", "法兰克福"),
    UEW1(17, "eu-west-1", "eu", "英国（伦敦）1", "伦敦"),
    MEE1(18, "me-east-1", "me", "中东东部1", "迪拜");

    Integer id;
    String region;
    String country;
    String area;
    String city;

    AliRegion(Integer id, String region, String country, String area, String city) {
        this.id = id;
        this.region = region;
        this.country = country;
        this.area = area;
        this.city = city;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }
}
