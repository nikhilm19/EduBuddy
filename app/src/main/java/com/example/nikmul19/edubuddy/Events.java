package com.example.nikmul19.edubuddy;

public class Events {
        private String title,desc;
        //private boolean absent;
        public Events(){

        }
        public Events(String title,String desc){
            this.title=title;
            this.desc=desc;
        }
        public String getTitle(){
            return this.title;
        }
        public String getDescription(){
            return this.desc;

        }
        //public boolean isAbsent(){return absent;}
        //public void setAbsent(boolean absent){this.absent=absent;}
        public void setTitle(String title){
            this.title=title;
        }
        public void setDescription(String desc){
            this.desc=desc;
        }

        }
