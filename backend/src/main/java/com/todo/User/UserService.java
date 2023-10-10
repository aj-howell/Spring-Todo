package com.todo.User;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.todo.Priority;
import com.todo.TodoDAO;
import com.todo.Email.GMailer;
import com.todo.Exceptions.ResourceNotFoundException;
import com.todo.PrayerDetails.PrayerDetailsFactory;
import com.todo.task.PrayerTask;
import com.todo.task.subtask.SubTask;

@Service
public class UserService {
    private final TodoDAO userDao;
    
    boolean notified = false;

    public UserService(@Qualifier("jdbc")TodoDAO userDao) {
        this.userDao = userDao;
    }

    public List<User> GetUsers() {
        return userDao.GetAllUsers();
    }

    public void CreateUser(UserRegistrationRequest user) {
        userDao.CreateUser(user);
    }

    public void EditUser(Integer userId, UserRegistrationRequest user) {
        userDao.GetUserById(userId)
        .orElseThrow( ()-> new ResourceNotFoundException("This user does not exist"));
        userDao.EditUser(userId, user);
    }

    public void DeleteUser(Integer userId) {
        userDao.GetUserById(userId)
        .orElseThrow( ()-> new ResourceNotFoundException("This user does not exist"));

        userDao.DeleteUserById(userId);
    }

    public User GetUser(Integer userId) {
        return userDao.GetUserById(userId)
        .orElseThrow( ()-> new ResourceNotFoundException("This user does not exist"));
    }

    public PrayerTask GetUserPrayer(Integer userId) {
    
       User selected =  userDao.GetUserById(userId)
        .orElseThrow( ()-> new ResourceNotFoundException("This user does not exist"));

       List<String> prayers = PrayerDetailsFactory.GetPrayerDetails(selected.getLocation().getCity_name().replaceAll("\\s+","%20"), userId);

        GMailer messenger = new GMailer(selected.getEmail());

       PrayerTask prayerTask = new PrayerTask();
       prayerTask.setDescription("Pray your 5 daily prayers at the prescribed times");
       prayerTask.setPriority(Priority.High.toString());
       prayerTask.setStartDate(LocalDate.now());
       prayerTask.setTaskName("Daily Prayers");
       prayerTask.setUser_id(userId);
       prayerTask.setTopic("Islam");

       LocalTime fajr = LocalTime.parse(prayers.get(0));

       LocalTime now = LocalTime.now();

       Duration timeAfterFajr = Duration.ofHours(now.getHour()-fajr.getHour());

       if(timeAfterFajr == Duration.ofHours(23)) // if it has been at least 23 hours then lets notify the user again
       {
         notified=false;
       }
        //  //will only send the email once if its a minute before fajr
        // if(now.isBefore(fajr.minusMinutes(1)))
        // {
        //   notified=false;
        // }

        for(int i=0; i<=prayers.size()-1; i++)
            {
                SubTask subTask = new SubTask();
                    if(i==0)
                    {
                        subTask.setTaskName("Fajr: "+prayers.get(i));
                    }
                    else if(i==1)
                    {
                        subTask.setTaskName("Dhuhr: "+prayers.get(i));
                    }
                    else if(i==2)
                    {
                        subTask.setTaskName("Asr: "+prayers.get(i));
                    }
                    else if(i==3)
                    {
                        subTask.setTaskName("Maghrib: "+prayers.get(i));
                    }
                    else
                    {
                        subTask.setTaskName("Isha: "+prayers.get(i));
                    }
                    subTask.setDescription("Make sure that you do not miss this prayer");
                    prayerTask.addSubtask(subTask);

                    try {
                        
                        if(notified==false)
                        {
                         messenger.sendEmail(subTask, "anazjai@gmail.com");   
                         System.out.println("email sent");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
            
            notified=true;

       return prayerTask;
    }
}
