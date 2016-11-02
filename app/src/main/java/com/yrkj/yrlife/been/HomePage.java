package com.yrkj.yrlife.been;

import java.util.List;

/**
 * Created by cjn on 2016/11/2.
 */

public class HomePage {

    /**
     * 首页信息
     */
    private HomePageBean homePage;

    public HomePageBean getHomePage() {
        return homePage;
    }

    public void setHomePage(HomePageBean homePage) {
        this.homePage = homePage;
    }

    public static class HomePageBean {
        /**
         * 历史消费板块
         * lastWashCode : 2
         */

        private HistoryWashConsumeSectionBean historyWashConsumeSection;
        /**
         * 系统消息板块
         * messageTitle : 测试标题1
         * messageTime : 2016-06-04 11:50:03
         */

        private List<SystemMessageSectionBean> systemMessageSection;
        /**
         * 顶部banner图板块
         */
        private List<String> bannerSection;
        /**
         * 评价板块 评价板块
         */
        private List<RemarkStarSectionBean> remarkStarSection;
        /**
         * 洗车记录板块
         */
        private List<WashMessageSectionBean> washMessageSection;
        /**
         * 正在运行的洗车机板块
         * detailUrl : http://139.129.53.70/wmmanager/static/machineDetail/1477474301059.html
         * machine_number : 672543
         * machine_name : 卓越蔚蓝群岛（顺通达商务服务有限公司）
         * phone : 15166600751
         * machineImages : []
         * qrCodeUrl : http://139.129.53.70/wmmanager/upload/20161026173137137.png
         * lng : 120.346569
         * machine_pic : http://139.129.53.70/wmmanager/upload/20160914180423460.jpg
         * isWashing : 1
         * id : 38
         * address : 山东省青岛市城阳区青岛市城阳区卓越蔚蓝群岛（顺通达商务服务有限公司）
         * orders : 126
         * lat : 36.268948
         */

        private List<RunningMachineSectionBean> runningMachineSection;

        public HistoryWashConsumeSectionBean getHistoryWashConsumeSection() {
            return historyWashConsumeSection;
        }

        public void setHistoryWashConsumeSection(HistoryWashConsumeSectionBean historyWashConsumeSection) {
            this.historyWashConsumeSection = historyWashConsumeSection;
        }

        public List<SystemMessageSectionBean> getSystemMessageSection() {
            return systemMessageSection;
        }

        public void setSystemMessageSection(List<SystemMessageSectionBean> systemMessageSection) {
            this.systemMessageSection = systemMessageSection;
        }

        public List<String> getBannerSection() {
            return bannerSection;
        }

        public void setBannerSection(List<String> bannerSection) {
            this.bannerSection = bannerSection;
        }

        public List<RemarkStarSectionBean> getRemarkStarSection() {
            return remarkStarSection;
        }

        public void setRemarkStarSection(List<RemarkStarSectionBean> remarkStarSection) {
            this.remarkStarSection = remarkStarSection;
        }

        public List<WashMessageSectionBean> getWashMessageSection() {
            return washMessageSection;
        }

        public void setWashMessageSection(List<WashMessageSectionBean> washMessageSection) {
            this.washMessageSection = washMessageSection;
        }

        public List<RunningMachineSectionBean> getRunningMachineSection() {
            return runningMachineSection;
        }

        public void setRunningMachineSection(List<RunningMachineSectionBean> runningMachineSection) {
            this.runningMachineSection = runningMachineSection;
        }

        public static class HistoryWashConsumeSectionBean {
            private int lastWashCode;

            public int getLastWashCode() {
                return lastWashCode;
            }

            public void setLastWashCode(int lastWashCode) {
                this.lastWashCode = lastWashCode;
            }
        }

        public static class SystemMessageSectionBean {
            private String messageTitle;
            private String messageTime;

            public String getMessageTitle() {
                return messageTitle;
            }

            public void setMessageTitle(String messageTitle) {
                this.messageTitle = messageTitle;
            }

            public String getMessageTime() {
                return messageTime;
            }

            public void setMessageTime(String messageTime) {
                this.messageTime = messageTime;
            }
        }

        public static class RemarkStarSectionBean {
            private String remarkTime;
            private String detailUrl;
            private String machine_number;
            private String machine_name;
            private String phone;
            private String userImage;
            private String qrCodeUrl;
            private String lng;
            private String machine_pic;
            private String isWashing;
            private int id;
            private String address;
            private int stars;
            private String userName;
            private int orders;
            private String lat;
            private List<String> machineImages;

            public String getRemarkTime() {
                return remarkTime;
            }

            public void setRemarkTime(String remarkTime) {
                this.remarkTime = remarkTime;
            }

            public String getDetailUrl() {
                return detailUrl;
            }

            public void setDetailUrl(String detailUrl) {
                this.detailUrl = detailUrl;
            }

            public String getMachine_number() {
                return machine_number;
            }

            public void setMachine_number(String machine_number) {
                this.machine_number = machine_number;
            }

            public String getMachine_name() {
                return machine_name;
            }

            public void setMachine_name(String machine_name) {
                this.machine_name = machine_name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getUserImage() {
                return userImage;
            }

            public void setUserImage(String userImage) {
                this.userImage = userImage;
            }

            public String getQrCodeUrl() {
                return qrCodeUrl;
            }

            public void setQrCodeUrl(String qrCodeUrl) {
                this.qrCodeUrl = qrCodeUrl;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getMachine_pic() {
                return machine_pic;
            }

            public void setMachine_pic(String machine_pic) {
                this.machine_pic = machine_pic;
            }

            public String getIsWashing() {
                return isWashing;
            }

            public void setIsWashing(String isWashing) {
                this.isWashing = isWashing;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getStars() {
                return stars;
            }

            public void setStars(int stars) {
                this.stars = stars;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public int getOrders() {
                return orders;
            }

            public void setOrders(int orders) {
                this.orders = orders;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public List<String> getMachineImages() {
                return machineImages;
            }

            public void setMachineImages(List<String> machineImages) {
                this.machineImages = machineImages;
            }
        }

        public static class WashMessageSectionBean {
            private String content;
            private String machinePicture;
            private String userImage;
            private String userName;
            private String washTime;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getMachinePicture() {
                return machinePicture;
            }

            public void setMachinePicture(String machinePicture) {
                this.machinePicture = machinePicture;
            }

            public String getUserImage() {
                return userImage;
            }

            public void setUserImage(String userImage) {
                this.userImage = userImage;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getWashTime() {
                return washTime;
            }

            public void setWashTime(String washTime) {
                this.washTime = washTime;
            }
        }

        public static class RunningMachineSectionBean {
            private String detailUrl;
            private String machine_number;
            private String machine_name;
            private String phone;
            private String qrCodeUrl;
            private String lng;
            private String machine_pic;
            private String isWashing;
            private int id;
            private String address;
            private int orders;
            private String lat;
            private List<?> machineImages;

            public String getDetailUrl() {
                return detailUrl;
            }

            public void setDetailUrl(String detailUrl) {
                this.detailUrl = detailUrl;
            }

            public String getMachine_number() {
                return machine_number;
            }

            public void setMachine_number(String machine_number) {
                this.machine_number = machine_number;
            }

            public String getMachine_name() {
                return machine_name;
            }

            public void setMachine_name(String machine_name) {
                this.machine_name = machine_name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getQrCodeUrl() {
                return qrCodeUrl;
            }

            public void setQrCodeUrl(String qrCodeUrl) {
                this.qrCodeUrl = qrCodeUrl;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getMachine_pic() {
                return machine_pic;
            }

            public void setMachine_pic(String machine_pic) {
                this.machine_pic = machine_pic;
            }

            public String getIsWashing() {
                return isWashing;
            }

            public void setIsWashing(String isWashing) {
                this.isWashing = isWashing;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getOrders() {
                return orders;
            }

            public void setOrders(int orders) {
                this.orders = orders;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public List<?> getMachineImages() {
                return machineImages;
            }

            public void setMachineImages(List<?> machineImages) {
                this.machineImages = machineImages;
            }
        }
    }
}
