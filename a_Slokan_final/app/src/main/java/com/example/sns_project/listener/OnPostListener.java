package com.example.sns_project.listener;

import com.example.sns_project.Postinfo;

public interface OnPostListener {
    void onDelete(Postinfo postinfo);
    void onModify();
}
