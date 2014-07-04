package ru.arcadoss.collagemaker.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import ru.arcadoss.collagemaker.web.InstagramAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by arcturus at 03.07.14
 "data":[
 {"attribution":null,"tags":[],"location":null,"comments":
 {"count":0,"data":[]},
 "filter":"Sierra","created_time":"1349870962","link":"http:\/\/instagram.com\/p\/QmexcypSBF\/",
 "likes": {"count":30,"data":[
 {"username":"rocketmind","profile_picture":"http:\/\/images.ak.instagram.com\/profiles\/profile_240269521_75sq_1387137293.jpg","id":"240269521","full_name":"www.rocketmind.ru"},
 {"username":"samsungru","profile_picture":"http:\/\/images.ak.instagram.com\/profiles\/profile_262813012_75sq_1354535126.jpg","id":"262813012","full_name":"Samsung Russia"},
 {"username":"sonywalkmanrussia","profile_picture":"http:\/\/images.ak.instagram.com\/profiles\/profile_370368334_75sq_1393491072.jpg","id":"370368334","full_name":"Sony Walkman Russia"},
 {"username":"perfectcook","profile_picture":"http:\/\/images.ak.instagram.com\/profiles\/profile_381878158_75sq_1368809527.jpg","id":"381878158","full_name":"\u041a\u043d\u0438\u0433\u0430 \u0420\u0435\u0446\u0435\u043f\u0442\u043e\u0432"}]
 },
 "images":
 {"low_resolution":
 {"url":"http:\/\/scontent-a.cdninstagram.com\/hphotos-xpa1\/outbound-distilleryimage9\/t0.0-17\/OBPTH\/5378d35a12d311e2ae7122000a1e87a9_6.jpg","width":306,"height":306},
 "thumbnail":
 {"url":"http:\/\/scontent-a.cdninstagram.com\/hphotos-xpa1\/outbound-distilleryimage9\/t0.0-17\/OBPTH\/5378d35a12d311e2ae7122000a1e87a9_5.jpg","width":150,"height":150},
 "standard_resolution":
 {"url":"http:\/\/scontent-a.cdninstagram.com\/hphotos-xpa1\/outbound-distilleryimage9\/t0.0-17\/OBPTH\/5378d35a12d311e2ae7122000a1e87a9_7.jpg","width":612,"height":612}},
 "users_in_photo":[],"caption":
 {"created_time":"1349870979","text":"\u0421\u0442\u0443\u043b \u0441\u043e\u0437\u0435\u0440\u0446\u0430\u043d\u0438\u044f","from":
 {"username":"arcadoss","profile_picture":"http:\/\/images.ak.instagram.com\/profiles\/profile_46172375_75sq_1336309909.jpg","id":"46172375","full_name":"arcadoss"},
 "id":"299061913812541633"},
 "type":"image","id":"299061764830863429_46172375","user":
 {"username":"arcadoss","website":"","profile_picture":"http:\/\/images.ak.instagram.com\/profiles\/profile_46172375_75sq_1336309909.jpg","full_name":"arcadoss","bio":"","id":"46172375"}
 },
 ...
 ]


 */
public class EntriesListWrapper {
	private final List<Entry> list;
	private boolean wasSorted;

	public EntriesListWrapper() {
		list = new ArrayList<Entry>();
		wasSorted = false;
	}

	public static class Deserializer extends JsonDeserializer<EntriesListWrapper> {
		private static final String TYPE_F = "type", LIKES_F = "likes", IMAGES_F = "images",
				COUNT_F = "count";

		@Override
		public EntriesListWrapper deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			EntriesListWrapper out = new EntriesListWrapper();
			ObjectMapper mapper = InstagramAdapter.getConverter().getObjectMapper();

			JsonToken token = jp.nextToken();
			assert (token == JsonToken.START_ARRAY);

			while (token != JsonToken.END_ARRAY) {
				TreeNode node = jp.getCodec().readTree(jp);

				if (((TextNode) node.get(TYPE_F)).asText().equals("image")) {
					int likesCount = ((IntNode) node.get(LIKES_F).get(COUNT_F)).intValue();
//					EntryPhotos photoInfo = ctxt.readValue(node.get(IMAGES_F).traverse(), EntryPhotos.class);
					EntryPhotos photoInfo = mapper.treeToValue(node.get(IMAGES_F), EntryPhotos.class);

					out.list.add(new Entry(likesCount, photoInfo));
				}

				token = jp.nextToken();
			}

			assert (token == JsonToken.END_ARRAY);
			return out;
		}
	}

	public List<Entry> getSorted() {
		if (!wasSorted) {
			Collections.sort(list, Entry.LIKES_COMPARATOR);
			wasSorted = true;
		}
		return list;
	}
}
