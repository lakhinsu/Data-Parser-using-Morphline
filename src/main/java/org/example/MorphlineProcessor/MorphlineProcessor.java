package org.example.MorphlineProcessor;

import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Compiler;
import org.kitesdk.morphline.base.Fields;
import org.example.MorphlineCommands.WriteToInflux;

import java.io.File;

public class MorphlineProcessor {
    Command morphline;

    public MorphlineProcessor(String morphlineFilePath, String morphlineId) {
        File configFile = new File(morphlineFilePath);
        MorphlineContext context = new MorphlineContext.Builder().build();
        this.morphline = new Compiler().compile(configFile, morphlineId, context, null);
    }

    public boolean Process(String message) {
        Record record = new Record();
        record.put(Fields.MESSAGE, message);
        return this.morphline.process(record);
    }
}
