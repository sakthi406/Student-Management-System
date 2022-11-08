/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.*;
import java.sql.Connection;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author praveen vuddagiri
 */
public class dbms {
    Connection conn=null;
    Statement st;
    ResultSet r;
    String userId,password;
    String sid,sname,sgen,sdob,sphone,sage,semail,sadd;
    String tid,tname,tgen,tdob,tphone,tage,temail,tpass="",tsub;
    String month,speak;
    float attend_per;
    int os=0,cn=0,dbms=0,lt=0,se=0,tot=0,year;
    float per=0;

    void connect(){
        try{
            Class.forName("java.sql.DriverManager");
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/college","root","praveen");
            st = conn.createStatement();
            System.out.println("Connection EStablished");//to be removed.
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }
    void aivoice(){
        System.setProperty("freetts.voices","com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice;
        voice = VoiceManager.getInstance().getVoice("kevin16");
       
        Voice []voicelist = VoiceManager.getInstance().getVoices();
        for(int i=0;i<voicelist.length;i++)
        {
            //System.out.println("#Voice: "+voicelist[i].getName());
        }
       
        if(voice != null)
        {
            voice.allocate();
            voice.setPitch(100);
            voice.setVolume(10);
            voice.setRate(160);
//            System.out.println("Voice Rate: "+voice.getRate());
//             System.out.println("Voice Pitch: ");
//             System.out.println("Voice Volume: "+voice.getVolume());
             boolean status = voice.speak(speak);
             voice.deallocate();
        }
        else
        {
            System.out.println("Error in getting Voices");
        }
    }
    void getcre(){
        try{
            st.execute("SELECT * FROM admincre;");
            r=st.getResultSet();
            while(r.next()){
                userId=r.getString("id");
                password = r.getString("password");
            }
            //System.out.println("Your Password and userid is :"+userId+"   "+password);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    void addstudent(){
        try{
            String qur="INSERT INTO STUD_DETAILS VALUES('"+sid+"','"+sname+"','"+sgen+"','"+sdob+"','"+sphone+"','"+sage+"','"+semail+"','"+sadd+"');";
            st.execute(qur);
            qur="INSERT INTO UT1 VALUES('"+sid+"',0,0,0,0,0,0,0);";
            st.execute(qur);
            qur="INSERT INTO UT2 VALUES('"+sid+"',0,0,0,0,0,0,0);";
            st.execute(qur);
            qur="INSERT INTO UT3 VALUES('"+sid+"',0,0,0,0,0,0,0);";
            st.execute(qur);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    void deletestudent(){
        try{
            String qur="DELETE FROM STUD_DETAILS WHERE Reg_id='"+sid+"';";
            st.execute(qur);
            qur="DELETE FROM UT1 WHERE Reg_id='"+sid+"';";
            st.execute(qur);
            qur="DELETE FROM UT2 WHERE Reg_id='"+sid+"';";
            st.execute(qur);
            qur="DELETE FROM UT3 WHERE Reg_id='"+sid+"';";
            st.execute(qur);
            qur="DELETE FROM ATTENDENCE WHERE Reg_id='"+sid+"';";
            st.execute(qur);
            qur="DELETE FROM attendence WHERE Reg_id='"+sid+"';";
            st.execute(qur);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }
    
    void getStudent(){
        try {
            String qur = "SELECT * FROM STUD_DETAILS WHERE Reg_id='"+sid+"';";
            sid="";
            st.execute(qur);
            r=st.getResultSet();
            boolean i=r.next();
            if(i==false)
                JOptionPane.showMessageDialog(null,"Student not found in the Database!!","Alert",JOptionPane.WARNING_MESSAGE);
            while(i){
                sid=r.getString("Reg_id");
                sname=r.getString("name");
                sgen=r.getString("gender");
                sdob=r.getString("dob");
                sphone=r.getString("phone");
                sage=r.getString("age");
                semail=r.getString("email");
                sadd=r.getString("address");
                i=r.next();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    void updatestudent(){
        try {
            //should call getStudent before this function.
            deletestudent();//example had to be removed.
            addstudent();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    void addattend(){
        getStudent();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
        LocalDateTime now = LocalDateTime.now();
        year=Integer.parseInt(dtf.format(now));
        try {
            String qur="INSERT INTO ATTENDENCE VALUES('"+sid+"','"+month+"',"+year+","+per+");";
            st.execute(qur);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }

    }

    void insert_ut1(String sub,int marks){
        getStudent();
        try {
            String qur = "SELECT * FROM UT1 WHERE Reg_id='"+sid+"';";
            st.execute(qur);
            r=st.getResultSet();
            while(r.next()){
                sid=r.getString("Reg_id");
                os=Integer.parseInt(r.getString("os"));
                cn=Integer.parseInt(r.getString("cn"));
                dbms=Integer.parseInt(r.getString("dbms"));
                lt=Integer.parseInt(r.getString("lt"));
                se=Integer.parseInt(r.getString("se"));
            }
            qur="UPDATE UT1 set "+sub+"="+marks+" WHERE Reg_id='"+sid+"';";
            st.execute(qur);

            qur = "SELECT * FROM UT1 WHERE Reg_id='"+sid+"';";
            st.execute(qur);
            r=st.getResultSet();
            while(r.next()){
                sid=r.getString("Reg_id");
                os=Integer.parseInt(r.getString("os"));
                cn=Integer.parseInt(r.getString("cn"));
                dbms=Integer.parseInt(r.getString("dbms"));
                lt=Integer.parseInt(r.getString("lt"));
                se=Integer.parseInt(r.getString("se"));
            }
            tot=os+cn+dbms+lt+se;
            per=tot;
            qur="UPDATE UT1 set per="+per+",tot="+tot+" WHERE Reg_id='"+sid+"';";
            st.execute(qur);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    void insert_ut2(String sub,int marks){
        getStudent();
        try {
            String qur = "SELECT * FROM UT2 WHERE Reg_id='"+sid+"';";
            st.execute(qur);
            r=st.getResultSet();
            while(r.next()){
                sid=r.getString("Reg_id");
                os=Integer.parseInt(r.getString("os"));
                cn=Integer.parseInt(r.getString("cn"));
                dbms=Integer.parseInt(r.getString("dbms"));
                lt=Integer.parseInt(r.getString("lt"));
                se=Integer.parseInt(r.getString("se"));
            }
            qur="UPDATE UT2 set "+sub+"="+marks+" WHERE Reg_id='"+sid+"';";
            st.execute(qur);
    
            qur = "SELECT * FROM UT2 WHERE Reg_id='"+sid+"';";
            st.execute(qur);
            r=st.getResultSet();
            while(r.next()){
                sid=r.getString("Reg_id");
                os=Integer.parseInt(r.getString("os"));
                cn=Integer.parseInt(r.getString("cn"));
                dbms=Integer.parseInt(r.getString("dbms"));
                lt=Integer.parseInt(r.getString("lt"));
                se=Integer.parseInt(r.getString("se"));
            }
            tot=os+cn+dbms+lt+se;
            per=tot;
            qur="UPDATE UT2 set per="+per+",tot="+tot+" WHERE Reg_id='"+sid+"';";
            st.execute(qur);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    void insert_ut3(String sub,int marks){
        getStudent();
        try {
            String qur = "SELECT * FROM UT3 WHERE Reg_id='"+sid+"';";
            st.execute(qur);
            r=st.getResultSet();
            while(r.next()){
                sid=r.getString("Reg_id");
                os=Integer.parseInt(r.getString("os"));
                cn=Integer.parseInt(r.getString("cn"));
                dbms=Integer.parseInt(r.getString("dbms"));
                lt=Integer.parseInt(r.getString("lt"));
                se=Integer.parseInt(r.getString("se"));
            }
            qur="UPDATE UT3 set "+sub+"="+marks+" WHERE Reg_id='"+sid+"';";
            st.execute(qur);
    
            qur = "SELECT * FROM UT3 WHERE Reg_id='"+sid+"';";
            st.execute(qur);
            r=st.getResultSet();
            while(r.next()){
                sid=r.getString("Reg_id");
                os=Integer.parseInt(r.getString("os"));
                cn=Integer.parseInt(r.getString("cn"));
                dbms=Integer.parseInt(r.getString("dbms"));
                lt=Integer.parseInt(r.getString("lt"));
                se=Integer.parseInt(r.getString("se"));
            }
            tot=os+cn+dbms+lt+se;
            per=tot;
            qur="UPDATE UT3 set per="+per+",tot="+tot+" WHERE Reg_id='"+sid+"';";
            st.execute(qur);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    void getTeacherId(){
        try {
            String qur=("SELECT COUNT(ID) FROM TEACHER;");
            st.execute(qur);
            r=st.getResultSet();
            int count=0;
            tpass="";
            tid="";
            while(r.next()){
                count=r.getInt("COUNT(ID)");
            }
            if(count<1){
                tid="1001";
            }
            else{
                qur=("SELECT MAX(ID) FROM TEACHER;");
                st.execute(qur);
                r=st.getResultSet();
                while(r.next()){
                    count=Integer.parseInt(r.getString("MAX(ID)"));
                }
                tid=String.valueOf((count+1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    void addTeacher(){
        try{
            tpass="";
            for (int i = 0; i < 5; i++) {
                if(i<3){
                    tpass+=tname.charAt(i);
                }
                else{
                    tpass+=tid.charAt(i-3);
                }
            }
            String qur="INSERT INTO TEACHER VALUES('"+tid+"','"+tname+"','"+tgen+"','"+tdob+"',"+tage+",'"+tphone+"','"+temail+"','"+tsub+"','"+tpass+"');";
            st.execute(qur);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    void deleteTeacher(){
        try{
            String qur="DELETE FROM TEACHER WHERE id='"+tid+"';";
            st.execute(qur);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    void updateTeacher(){
        try {
            deleteTeacher();
            addTeacher();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    void getTeacher(){
        try {
            String qur = "SELECT * FROM TEACHER WHERE id='"+tid+"';";
            st.execute(qur);
            tid="";
            r=st.getResultSet();
            boolean i=r.next();
            if(i==false)
                JOptionPane.showMessageDialog(null,"Teacher not found in the Database!!","Alert",JOptionPane.WARNING_MESSAGE);
            
            while(i){
                tid=r.getString("id");
                tname=r.getString("name");
                tgen=r.getString("gender");
                tdob=r.getString("dob");
                tage=r.getString("age");
                tphone=r.getString("phone");
                temail=r.getString("email");
                tsub=r.getString("subject");
                tpass=r.getString("password");
                i=r.next();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }
    void getTeachPass(){
        try {
            tid="1001";
            String qur = "SELECT password FROM TEACHER WHERE id='"+tid+"';";
            st.execute(qur);
            r=st.getResultSet();
            
            tpass=r.getString("password");
            System.out.println(tpass);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e,"Alert",JOptionPane.WARNING_MESSAGE);
        }
    }
    public static void main(String[] args) {
        dbms d = new dbms();
        d.connect();
        d.getcre();
        d.aivoice();
        d.getTeachPass();
    }
}
