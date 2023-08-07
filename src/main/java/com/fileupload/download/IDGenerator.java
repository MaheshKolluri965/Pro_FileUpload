package com.fileupload.download;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class IDGenerator implements IdentifierGenerator {

    private static final String PREFIX = "MJ"; // Prefix for the generated IDs
    private static long sequence = 001; // Start value for the sequence

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String generatedId = PREFIX + sequence;
        sequence++; // Increment the sequence for the next ID
        return generatedId;
    }
}

