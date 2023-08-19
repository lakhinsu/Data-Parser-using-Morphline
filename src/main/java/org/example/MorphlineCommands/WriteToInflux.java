package org.example.MorphlineCommands;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.typesafe.config.Config;
import org.example.InfluxDB.InfluxDBConnector;
import org.example.InfluxDB.InfluxDBWriter;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.CommandBuilder;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.base.AbstractCommand;

import java.util.*;

public class WriteToInflux implements CommandBuilder {
    @Override
    public Collection<String> getNames() {
        return Collections.singletonList("writeToInflux");
    }

    @Override
    public Command build(Config config, Command parent, Command child, MorphlineContext context) {
        return new WriteToInfluxCommand(this, config, parent, child, context);
    }

    ///////////////////////////////////////////////////////////////////////////////
    // Nested classes:
    ///////////////////////////////////////////////////////////////////////////////
    private static final class WriteToInfluxCommand extends AbstractCommand {

        private final String bucketName;
        private final String bucketToken;
        private final String org;
        private final String url;
        private final List<String> dataFields;

        private final InfluxDBWriter influxDBWriter;
        private final List<String> tagFields;
        private final String measurementName;
        private final String timeField;

        public WriteToInfluxCommand(CommandBuilder builder, Config config, Command parent, Command child, MorphlineContext context) {
            super(builder, config, parent, child, context);
            this.bucketName = getConfigs().getString(config, "bucketName");
            this.dataFields = getConfigs().getStringList(config, "dataFields");
            this.tagFields = getConfigs().getStringList(config, "tagFields");
            this.measurementName = getConfigs().getString(config, "measurementName");
            this.timeField = getConfigs().getString(config, "timeField");

            this.bucketToken = System.getenv("INFLUXDB_TOKEN");
            this.org = System.getenv("INFLUXDB_ORG");
            this.url = System.getenv("INFLUXDB_URL");

            InfluxDBConnector influxDBConnector = new InfluxDBConnector(this.bucketToken, this.url, this.bucketName, this.org);
            this.influxDBWriter = new InfluxDBWriter(influxDBConnector);
            validateArguments();
        }

        @Override
        @SuppressWarnings("unchecked")
        protected boolean doProcess(Record record) {

            Point point = new Point(this.measurementName);
            // Create tags from tagFields
            Map<String, String> tags = new HashMap<>();
            for(String tagField: this.tagFields) {
                tags.put(tagField, (String) record.getFirstValue(tagField));
            }
            point.addTags(tags);

            // Create fields
            Map<String, Object> fields = new HashMap<>();
            for(String field: this.dataFields) {
                fields.put(field, record.getFirstValue(field));
            }
            point.addFields(fields);

            // Add time
            Long timeStamp = Long.parseLong(record.getFirstValue(this.timeField).toString());
            point.time(timeStamp, WritePrecision.MS);

            this.influxDBWriter.writePoint(point);
            // pass record to next command in chain:
            return super.doProcess(record);
        }

    }
}
