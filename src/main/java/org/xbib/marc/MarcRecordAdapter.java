/*
   Copyright 2016 Jörg Prante

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.xbib.marc;

import org.xbib.marc.label.RecordLabel;

/**
 * The Marc record adapter collects Marc field events, collects them in a Marc builder,
 * and sends collected records to a Marc record listener. This is very handy for
 * redirecting Marc parser to Marc writers that operate record-by-record.
 */
public class MarcRecordAdapter implements MarcListener {

    protected final MarcRecordListener marcRecordListener;

    protected Marc.Builder builder;

    public MarcRecordAdapter(MarcRecordListener marcRecordListener) {
        this.marcRecordListener = marcRecordListener;
        this.builder = Marc.builder();
    }

    @Override
    public void beginCollection() {
        marcRecordListener.beginCollection();
    }

    @Override
    public void beginRecord(String format, String type) {
        builder.setFormat(format);
        builder.setType(type);
    }

    @Override
    public void leader(String label) {
        builder.recordLabel(RecordLabel.builder().from(label.toCharArray()).build());
    }

    @Override
    public void field(MarcField field) {
        builder.addField(field);
    }

    @Override
    public void endRecord() {
        marcRecordListener.record(builder.buildRecord());
        builder = Marc.builder();
    }

    @Override
    public void endCollection() {
        marcRecordListener.endCollection();
    }
}
