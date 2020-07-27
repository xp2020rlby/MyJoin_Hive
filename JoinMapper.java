package MyJoin;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class JoinMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {

	private OrderBean ob = new OrderBean();
	private String fileName;

	@Override
	protected void setup(Mapper<LongWritable, Text, OrderBean, NullWritable>.Context context)
			throws IOException, InterruptedException {

		FileSplit fs = (FileSplit) context.getInputSplit();//读入文件获取文件名
		fileName = fs.getPath().getName();

		super.setup(context);//解析缓存中的数据
	}

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, OrderBean, NullWritable>.Context context)
			throws IOException, InterruptedException {

		String[] fields = value.toString().split(" ");
		// OID\ODATA\PID\PNAME\PRICE\OAMOUNT
		if (fileName.equals("product.txt")) {//product表数据赋值
			ob.setAll("", "", fields[0], fields[1], fields[2], "");
		} else {//order表数据赋值
			ob.setAll(fields[0], fields[1], fields[2], "", "", fields[3]);
		}
		context.write(ob, NullWritable.get());

	}

}
