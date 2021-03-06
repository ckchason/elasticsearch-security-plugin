package org.elasticsearch.plugins.security.util;

import java.io.IOException;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.rest.XContentRestResponse;
import org.elasticsearch.rest.XContentThrowableRestResponse;
import org.elasticsearch.rest.action.support.RestXContentBuilder;

public class SecurityUtil {

	private static final ESLogger log = Loggers.getLogger(SecurityUtil.class);

	private SecurityUtil() {

	}

	public static void send(final RestRequest request,
			final RestChannel channel, final RestStatus status, final String arg) {
		try {
			final XContentBuilder builder = RestXContentBuilder
					.restContentBuilder(request);
			builder.startObject();
			builder.field("status", status.getStatus());

			if (arg != null && !arg.isEmpty()) {
				builder.field("message", arg);
			}

			builder.endObject();
			channel.sendResponse(new XContentRestResponse(request, status,
					builder));
		} catch (final Exception e) {
			log.error("Failed to send a response.", e);
			try {
				channel.sendResponse(new XContentThrowableRestResponse(request,
						e));
			} catch (final IOException e1) {
				log.error("Failed to send a failure response.", e1);
			}
		}
	}

}
