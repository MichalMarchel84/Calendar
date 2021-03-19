package view;

import model.RepetitiveModel;

import java.time.LocalDateTime;

abstract class RepetitiveEntry extends Entry{

    private LocalDateTime time;

    RepetitiveEntry(RepetitiveModel model, LocalDateTime time) {
        super(model);
        this.time = time;
    }

    @Override
    LocalDateTime getTime(){
        return time;
    }

    @Override
    LocalDateTime getTimeEnd() {
        return time;
    }

    LocalDateTime getStartAt(){
        return getModel().getTime();
    }

    LocalDateTime getFinishedAt(){
        return getModel().getFinishedAt();
    }

    @Override
    void setTime(LocalDateTime time){
        this.time = time;
    }

    void setStartAt(LocalDateTime time){
        getModel().setTime(time);
    }

    void setFinishedAt(LocalDateTime time){
        getModel().setFinishedAt(time);
    }

    @Override
    protected RepetitiveModel getModel(){
        return (RepetitiveModel) super.getModel();
    }
}
