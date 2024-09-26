require 'logger'

# Configure logger with consistent timestamp format
logger = Logger.new(STDERR)
# Remove the datetime_format as it is not used with a custom formatter
# logger.datetime_format = '%Y-%m-%dT%H:%M:%S%z'

# Define a custom formatter proc with explicit datetime formatting
logger.formatter = proc do |severity, datetime, _progname, msg|
  "#{datetime.strftime('%Y-%m-%dT%H:%M:%S%z')} #{severity} #{msg}\n"
end

def emit_error(logger)
  error_types = %w[NoMethodError TypeError ArgumentError RuntimeError]
  error = error_types.sample

  begin
    case error
    when 'NoMethodError'
      nil.some_method
    when 'TypeError'
      'string' + 5
    when 'ArgumentError'
      Integer('invalid')
    when 'RuntimeError'
      raise 'Generic runtime error'
    end
  rescue StandardError => e
    logger.error("An error occurred: #{e}\n#{e.backtrace.join("\n")}")
  end
end

loop do
  emit_error(logger)
  sleep 60
end
