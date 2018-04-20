package com.ai.injection.service;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ai.cms.injection.bean.ADI;
import com.ai.cms.injection.bean.MappingBean;
import com.ai.cms.injection.bean.MovieBean;
import com.ai.cms.injection.bean.PictureBean;
import com.ai.cms.injection.bean.ProgramBean;
import com.ai.cms.injection.bean.SeriesBean;

public class XMLParser {

	public static ADI parser(String filePath) throws MalformedURLException,
			DocumentException {
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		Element root = document.getRootElement();

		ADI adi = new ADI();
		Element objects = root.element("Objects");
		parserObject(objects, adi);
		Element mappings = root.element("Mappings");
		parserMapping(mappings, adi);

		String priority = root.attributeValue("Priority");
		adi.setPriority(org.apache.commons.lang3.math.NumberUtils.toInt(
				priority, 1));
		return adi;
	}

	@SuppressWarnings("unchecked")
	public static void parserMapping(Element mappings, ADI adi)
			throws MalformedURLException, DocumentException {
		if (mappings == null) {
			return;
		}
		List<Element> mappingList = mappings.elements();
		for (Element mapping : mappingList) {
			MappingBean mappingBean = new MappingBean();

			mappingBean.setID(mapping.attributeValue("ID"));
			mappingBean.setAction(mapping.attributeValue("Action"));
			mappingBean.setParentType(mapping.attributeValue("ParentType"));
			mappingBean.setElementType(mapping.attributeValue("ElementType"));
			mappingBean.setParentID(mapping.attributeValue("ParentID"));
			mappingBean.setElementID(mapping.attributeValue("ElementID"));
			mappingBean.setParentCode(mapping.attributeValue("ParentCode"));
			mappingBean.setElementCode(mapping.attributeValue("ElementCode"));

			if (StringUtils.isEmpty(mappingBean.getAction())) {
				continue;
			}
			if (StringUtils.isEmpty(mappingBean.getElementType())
					|| StringUtils.isEmpty(mappingBean.getElementCode())) {
				continue;
			}
			if (StringUtils.isEmpty(mappingBean.getParentType())
					|| StringUtils.isEmpty(mappingBean.getParentCode())) {
				continue;
			}

			List<Element> propertyList = mapping.elements();
			for (Element property : propertyList) {
				if (property.attributeValue("Name").equalsIgnoreCase("Type")) {
					mappingBean.setType(property.getText());
				} else if (property.attributeValue("Name").equalsIgnoreCase(
						"Sequence")) {
					mappingBean.setSequence(property.getText());
				} else if (property.attributeValue("Name").equalsIgnoreCase(
						"ValidStart")) {
					mappingBean.setValidStart(property.getText());
				} else if (property.attributeValue("Name").equalsIgnoreCase(
						"ValidEnd")) {
					mappingBean.setValidEnd(property.getText());
				} else if (property.attributeValue("Name").equalsIgnoreCase(
						"Result")) {
					mappingBean.setResult(property.getText());
				} else if (property.attributeValue("Name").equalsIgnoreCase(
						"ErrorDescription")) {
					mappingBean.setErrorDescription(property.getText());
				}
			}

			MappingBean mappingBean2 = new MappingBean();
			mappingBean2.setID(mappingBean.getID());
			mappingBean2.setAction(mappingBean.getAction());

			mappingBean2.setType(mappingBean.getType());
			mappingBean2.setSequence(mappingBean.getSequence());

			mappingBean2.setValidStart(mappingBean.getValidStart());
			mappingBean2.setValidEnd(mappingBean.getValidEnd());

			mappingBean2.setResult(mappingBean.getResult());
			mappingBean2.setErrorDescription(mappingBean.getErrorDescription());

			mappingBean2.setParentType(mappingBean.getElementType());
			mappingBean2.setElementType(mappingBean.getParentType());
			mappingBean2.setParentID(mappingBean.getElementID());
			mappingBean2.setElementID(mappingBean.getParentID());
			mappingBean2.setParentCode(mappingBean.getElementCode());
			mappingBean2.setElementCode(mappingBean.getParentCode());

			adi.getMappings().add(mappingBean);
			adi.getMappings().add(mappingBean2);
		}
	}

	@SuppressWarnings("unchecked")
	public static void parserObject(Element objects, ADI adi)
			throws MalformedURLException, DocumentException {
		if (objects == null) {
			return;
		}
		List<Element> objectList = objects.elements();
		for (Element object : objectList) {

			if (StringUtils.isEmpty(object.attributeValue("Action"))) {
				continue;
			}
			if (StringUtils.isEmpty(object.attributeValue("ElementType"))
					|| StringUtils.isEmpty(object.attributeValue("Code"))) {
				continue;
			}

			if (object.attributeValue("ElementType").equalsIgnoreCase("Series")) {
				SeriesBean seriesBean = new SeriesBean();
				adi.getObjects().add(seriesBean);

				seriesBean.setElementType(object.attributeValue("ElementType"));
				seriesBean.setAction(object.attributeValue("Action"));
				seriesBean.setID(object.attributeValue("ID"));
				seriesBean.setCode(object.attributeValue("Code"));

				List<Element> propertyList = object.elements();
				for (Element property : propertyList) {
					if (property.attributeValue("Name")
							.equalsIgnoreCase("Name")) {
						seriesBean.setName(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("OrderNumber")) {
						seriesBean.setOrderNumber(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("OriginalName")) {
						seriesBean.setOriginalName(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("SortName")) {
						seriesBean.setSortName(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("SearchName")) {
						seriesBean.setSearchName(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Genre")) {
						seriesBean.setGenre(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ActorDisplay")) {
						seriesBean.setActorDisplay(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("WriterDisplay")) {
						seriesBean.setWriterDisplay(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("OriginalCountry")) {
						seriesBean.setOriginalCountry(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Language")) {
						seriesBean.setLanguage(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ReleaseYear")) {
						seriesBean.setReleaseYear(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("OrgAirDate")) {
						seriesBean.setOrgAirDate(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("LicensingWindowStart")) {
						seriesBean.setLicensingWindowStart(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("LicensingWindowEnd")) {
						seriesBean.setLicensingWindowEnd(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("DisplayAsNew")) {
						seriesBean.setDisplayAsNew(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("DisplayAsLastChance")) {
						seriesBean.setDisplayAsLastChance(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Macrovision")) {
						seriesBean.setMacrovision(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Description")) {
						seriesBean.setDescription(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Status")) {
						seriesBean.setStatus(property.getText());

					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("VolumnCount")) {
						seriesBean.setVolumnCount(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("SeriesType")) {
						seriesBean.setSeriesType(property.getText());

					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Type")) {
						seriesBean.setType(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Keywords")) {
						seriesBean.setKeywords(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Tags")) {
						seriesBean.setTags(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reserve1")) {
						seriesBean.setReserve1(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reserve2")) {
						seriesBean.setReserve2(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reserve3")) {
						seriesBean.setReserve3(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reserve4")) {
						seriesBean.setReserve4(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reserve5")) {
						seriesBean.setReserve5(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("RMediaCode")) {
						seriesBean.setRMediaCode(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Result")) {
						seriesBean.setResult(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ErrorDescription")) {
						seriesBean.setErrorDescription(property.getText());

					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Kpeople")) {
						seriesBean.setKpeople(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Director")) {
						seriesBean.setDirector(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ScriptWriter")) {
						seriesBean.setScriptWriter(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Compere")) {
						seriesBean.setCompere(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Guest")) {
						seriesBean.setGuest(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reporter")) {
						seriesBean.setReporter(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("OPIncharge")) {
						seriesBean.setOPIncharge(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("VSPCode")) {
						seriesBean.setVSPCode(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("SPCode")) {
						seriesBean.setSPCode(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ContentProvider")) {
						seriesBean.setContentProvider(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("CopyRight")) {
						seriesBean.setCopyRight(property.getText());
					}
				}
			} else if (object.attributeValue("ElementType").equalsIgnoreCase(
					"Program")) {
				ProgramBean programBean = new ProgramBean();
				adi.getObjects().add(programBean);

				programBean
						.setElementType(object.attributeValue("ElementType"));
				programBean.setAction(object.attributeValue("Action"));
				programBean.setID(object.attributeValue("ID"));
				programBean.setCode(object.attributeValue("Code"));

				List<Element> propertyList = object.elements();
				for (Element property : propertyList) {
					if (property.attributeValue("Name")
							.equalsIgnoreCase("Name")) {
						programBean.setName(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("OrderNumber")) {
						programBean.setOrderNumber(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("OriginalName")) {
						programBean.setOriginalName(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("SortName")) {
						programBean.setSortName(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("SearchName")) {
						programBean.setSearchName(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Genre")) {
						programBean.setGenre(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ActorDisplay")) {
						programBean.setActorDisplay(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("WriterDisplay")) {
						programBean.setWriterDisplay(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("OriginalCountry")) {
						programBean.setOriginalCountry(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Language")) {
						programBean.setLanguage(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ReleaseYear")) {
						programBean.setReleaseYear(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("OrgAirDate")) {
						programBean.setOrgAirDate(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("LicensingWindowStart")) {
						programBean.setLicensingWindowStart(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("LicensingWindowEnd")) {
						programBean.setLicensingWindowEnd(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("DisplayAsNew")) {
						programBean.setDisplayAsNew(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("DisplayAsLastChance")) {
						programBean.setDisplayAsLastChance(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Macrovision")) {
						programBean.setMacrovision(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Description")) {
						programBean.setDescription(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Status")) {
						programBean.setStatus(property.getText());

					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("PriceTaxIn")) {
						programBean.setPriceTaxIn(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("SourceType")) {
						programBean.setSourceType(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("SeriesFlag")) {
						programBean.setSeriesFlag(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Definition")) {
						programBean.setDefinition(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("StorageType")) {
						programBean.setStorageType(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Duration")) {
						programBean.setDuration(property.getText());

					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Type")) {
						programBean.setType(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Keywords")) {
						programBean.setKeywords(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Tags")) {
						programBean.setTags(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reserve1")) {
						programBean.setReserve1(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reserve2")) {
						programBean.setReserve2(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reserve3")) {
						programBean.setReserve3(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reserve4")) {
						programBean.setReserve4(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reserve5")) {
						programBean.setReserve5(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("RMediaCode")) {
						programBean.setRMediaCode(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Result")) {
						programBean.setResult(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ErrorDescription")) {
						programBean.setErrorDescription(property.getText());

					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Kpeople")) {
						programBean.setKpeople(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Director")) {
						programBean.setDirector(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ScriptWriter")) {
						programBean.setScriptWriter(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Compere")) {
						programBean.setCompere(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Guest")) {
						programBean.setGuest(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Reporter")) {
						programBean.setReporter(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("OPIncharge")) {
						programBean.setOPIncharge(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("VSPCode")) {
						programBean.setVSPCode(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("SPCode")) {
						programBean.setSPCode(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ContentProvider")) {
						programBean.setContentProvider(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("CopyRight")) {
						programBean.setCopyRight(property.getText());
					}
				}
			} else if (object.attributeValue("ElementType").equalsIgnoreCase(
					"Movie")) {
				MovieBean movieBean = new MovieBean();
				adi.getObjects().add(movieBean);

				movieBean.setElementType(object.attributeValue("ElementType"));
				movieBean.setAction(object.attributeValue("Action"));
				movieBean.setID(object.attributeValue("ID"));
				movieBean.setCode(object.attributeValue("Code"));

				List<Element> propertyList = object.elements();
				for (Element property : propertyList) {
					if (property.attributeValue("Name")
							.equalsIgnoreCase("Type")) {
						movieBean.setType(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("FileURL")) {
						movieBean.setFileURL(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("SourceDRMType")) {
						movieBean.setSourceDRMType(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("DestDRMType")) {
						movieBean.setDestDRMType(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("AudioType")) {
						movieBean.setAudioType(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ScreenFormat")) {
						movieBean.setScreenFormat(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ClosedCaptioning")) {
						movieBean.setClosedCaptioning(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("OCSURL")) {
						movieBean.setOCSURL(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Duration")) {
						movieBean.setDuration(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("FileSize")) {
						movieBean.setFileSize(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("BitRateType")) {
						movieBean.setBitRateType(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("VideoType")) {
						movieBean.setVideoType(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("AudioFormat")) {
						movieBean.setAudioFormat(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Resolution")) {
						movieBean.setResolution(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("VideoProfile")) {
						movieBean.setVideoProfile(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("SystemLayer")) {
						movieBean.setSystemLayer(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ServiceType")) {
						movieBean.setServiceType(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Result")) {
						movieBean.setResult(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ErrorDescription")) {
						movieBean.setErrorDescription(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("MediaSpec")) {
						movieBean.setMediaSpec(property.getText());
					}
				}
			} else if (object.attributeValue("ElementType").equalsIgnoreCase(
					"Picture")) {
				PictureBean pictureBean = new PictureBean();
				adi.getObjects().add(pictureBean);

				pictureBean
						.setElementType(object.attributeValue("ElementType"));
				pictureBean.setAction(object.attributeValue("Action"));
				pictureBean.setID(object.attributeValue("ID"));
				pictureBean.setCode(object.attributeValue("Code"));

				List<Element> propertyList = object.elements();
				for (Element property : propertyList) {
					if (property.attributeValue("Name").equalsIgnoreCase(
							"FileURL")) {
						pictureBean.setFileURL(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Description")) {
						pictureBean.setDescription(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("Result")) {
						pictureBean.setResult(property.getText());
					} else if (property.attributeValue("Name")
							.equalsIgnoreCase("ErrorDescription")) {
						pictureBean.setErrorDescription(property.getText());
					}
				}
			}
		}
	}
}