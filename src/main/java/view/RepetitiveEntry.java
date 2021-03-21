package view;

import model.RepetitiveModel;

import java.time.LocalDateTime;

public abstract class RepetitiveEntry extends Entry{

    private LocalDateTime time;

    RepetitiveEntry(RepetitiveModel model, LocalDateTime time) {
        super(model);
        this.time = time;
    }

    @Override
    public LocalDateTime getTime(){
        return time;
    }

    public LocalDateTime getStartAt(){
        return getModel().getTime();
    }

    public LocalDateTime getFinishedAt(){
        return getModel().getFinishedAt();
    }

    @Override
    public void setTime(LocalDateTime time){

        if(isFirstOccurrence()){
            setStartAt(time);
        }
        this.time = time;
    }

    public void setStartAt(LocalDateTime time){
        getModel().setTime(time);
    }

    public void setFinishedAt(LocalDateTime time){
        getModel().setFinishedAt(time);
    }

    public LocalDateTime getPrevious(){
        if(getTime().equals(getStartAt())) return null;
        else if(getModel().getInterval() > 0) return time.minusDays(getModel().getInterval());
        else if(getModel().getInterval() == 0) return time.minusMonths(1);
        else if(getModel().getInterval() == -1) return time.minusYears(1);
        else return null;
    }

    public boolean isFirstOccurrence(){
        return time.equals(getStartAt());
    }

    @Override
    public RepetitiveModel getModel(){
        return (RepetitiveModel) super.getModel();
    }
}
