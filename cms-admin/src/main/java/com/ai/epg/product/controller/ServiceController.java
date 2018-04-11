package com.ai.epg.product.controller;
//package com.ai.product.controller;
//
//import java.util.*;
//
//import javax.servlet.http.HttpServletRequest;
//
//import com.ai.AdminGlobal;
//import com.ai.common.enums.ItemTypeEnum;
//import com.ai.content.bean.BatchServiceBean;
//import com.ai.content.slaveentity.Program;
//import com.ai.content.slaveentity.Series;
//import com.ai.content.slaverepository.ProgramRepository;
//import com.ai.content.slaverepository.SeriesRepository;
//import com.ai.product.slaveentity.ServiceItem;
//import com.ai.product.slaverepository.ServiceItemRepository;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.ai.common.bean.BaseResult;
//import com.ai.common.bean.PageInfo;
//import com.ai.common.bean.ResultCode;
//import com.ai.common.controller.AbstractController;
//import com.ai.common.enums.InjectionStatusEnum;
//import com.ai.common.enums.OnlineStatusEnum;
//import com.ai.common.enums.ServiceTypeEnum;
//import com.ai.common.utils.BeanInfoUtil;
//import com.ai.content.bean.BatchInjectionBean;
//import com.ai.content.bean.BatchStatusBean;
//import com.ai.injection.entity.InjectionPlatform;
//import com.ai.injection.repository.InjectionPlatformRepository;
//import com.ai.injection.service.InjectionService;
//import com.ai.product.slaveentity.Service;
//import com.ai.product.slaverepository.ServiceRepository;
//
//@Controller
//@RequestMapping(value = { "/service/service" })
//public class ServiceController extends AbstractController {
//
//	@Autowired
//	private ServiceRepository serviceRepository;
//
//	@Autowired
//	private InjectionPlatformRepository injectionPlatformRepository;
//
//	@Autowired
//	private InjectionService injectionService;
//
//	@Autowired
//	private ServiceItemRepository serviceItemRepository;
//
//	@Autowired
//	private ProgramRepository programRepository;
//
//	@Autowired
//	private SeriesRepository seriesRepository;
//
//	@RequestMapping(value = { "" })
//	public String list(Model model, HttpServletRequest request,
//			PageInfo pageInfo) {
//		Page<Service> page = find(request, pageInfo, serviceRepository);
//		model.addAttribute("page", page);
//
//		model.addAttribute("statusEnum", OnlineStatusEnum.values());
//		model.addAttribute("typeEnum", ServiceTypeEnum.values());
//		model.addAttribute("injectionStatusEnum", InjectionStatusEnum.values());
//		return "service/service/list";
//	}
//
//	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
//	public String toAdd(Model model) {
//		Service service = new Service();
//		model.addAttribute("service", service);
//
//		model.addAttribute("statusEnum", OnlineStatusEnum.values());
//		model.addAttribute("typeEnum", ServiceTypeEnum.values());
//		model.addAttribute("injectionStatusEnum", InjectionStatusEnum.values());
//
//		return "service/service/edit";
//	}
//
//	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
//	@ResponseBody
//	public BaseResult add(@RequestBody Service service) {
//		return edit(service, null);
//	}
//
//	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
//	public String toEdit(Model model, @PathVariable("id") Long id) {
//		Service service = serviceRepository.findOne(id);
//		model.addAttribute("service", service);
//
//		model.addAttribute("statusEnum", OnlineStatusEnum.values());
//		model.addAttribute("typeEnum", ServiceTypeEnum.values());
//		model.addAttribute("injectionStatusEnum", InjectionStatusEnum.values());
//		return "service/service/edit";
//	}
//
//	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
//	@ResponseBody
//	public BaseResult edit(@RequestBody Service service,
//			@PathVariable("id") Long id) {
//		if (id == null) {
//			serviceRepository.save(service);
//		} else {
//			Service serviceInfo = serviceRepository.findOne(service.getId());
//			BeanInfoUtil
//					.bean2bean(
//							service,
//							serviceInfo,
//							"code,name,type,sortName,searchName,rentalPeriod,orderNumber,"
//									+ "licensingWindowStart,licensingWindowEnd,price,status,description,tag,keyword,partnerItemCode");
//			serviceRepository.save(serviceInfo);
//		}
//		return new BaseResult();
//	}
//
//	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
//	@ResponseBody
//	public BaseResult delete(@PathVariable("id") Long id) {
//		serviceRepository.delete(id);
//		return new BaseResult();
//	}
//
//	@RequestMapping(value = { "check" }, produces = "application/json; charset=UTF-8")
//	@ResponseBody
//	public Object[] check(
//			@RequestParam(value = "id", required = false) Long id,
//			@RequestParam(value = "fieldId") String fieldId,
//			@RequestParam(value = "fieldValue") String fieldValue) {
//		boolean exist = checkCode(id, fieldValue);
//
//		Object[] jsonValidateReturn = new Object[3];
//		jsonValidateReturn[0] = fieldId;
//		jsonValidateReturn[1] = !exist;
//		if (!exist) {
//			jsonValidateReturn[2] = "可以使用!";
//		} else {
//			jsonValidateReturn[2] = "代码" + StringUtils.trim(fieldValue)
//					+ "已使用!";
//		}
//		return jsonValidateReturn;
//	}
//
//	private boolean checkCode(Long id, String code) {
//		boolean exist = false;
//		Service service = null;
//		if (StringUtils.isNotEmpty(code)) {
//			service = serviceRepository.findOneByCode(code);
//		}
//		if (service != null) {
//			if (id == null || id == -1 || service.getId().longValue() != id) {
//				exist = true;
//			}
//		}
//		return exist;
//	}
//	
//	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
//	@ResponseBody
//	public BaseResult status(@PathVariable("id") Long id) {
//		Service serviceInfo = serviceRepository.findOne(id);
//		if (serviceInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
//			serviceInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
//		} else {
//			serviceInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
//		}
//		serviceRepository.save(serviceInfo);
//		return new BaseResult();
//	}
//
//	@RequestMapping(value = { "batchChangeStatus" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
//	@ResponseBody
//	public BaseResult batchChangeStatus(@RequestBody BatchStatusBean batchBean) {
//		Integer itemType = batchBean.getItemType();
//		String itemIds = batchBean.getItemIds();
//		if (itemType == null || StringUtils.isEmpty(itemIds)) {
//			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
//		}
//		String[] itemIdArr = itemIds.split(",");
//		if (null != itemIdArr) {
//			for (String itemIdStr : itemIdArr) {
//				Long itemId = Long.valueOf(itemIdStr);
//				Service serviceInfo = serviceRepository
//						.findOne(itemId);
//				serviceInfo.setStatus(batchBean.getStatus());
//				serviceRepository.save(serviceInfo);
//			}
//		}
//		return new BaseResult();
//	}
//
//	@RequestMapping(value = { "batchInjection" }, method = RequestMethod.GET)
//	public String tobatchInjection(Model model,
//			@RequestParam(value = "itemType") Integer itemType,
//			@RequestParam(value = "itemIds") String itemIds) {
//
//		model.addAttribute("itemType", itemType);
//		model.addAttribute("itemIds", itemIds);
//
//		List<InjectionPlatform> injectionPlatformList = injectionPlatformRepository
//				.findAll();
//		model.addAttribute("injectionPlatformList", injectionPlatformList);
//
//		return "service/service/batchInjection";
//	}
//
//	@RequestMapping(value = { "batchInjection" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
//	@ResponseBody
//	public BaseResult batchInjection(@RequestBody BatchInjectionBean batchBean) {
//		Integer itemType = batchBean.getItemType();
//		String itemIds = batchBean.getItemIds();
//		if (itemType == null || StringUtils.isEmpty(itemIds)) {
//			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
//		}
//		Date currentTime = new Date();
//		String[] itemIdArr = itemIds.split(",");
//		if (null != itemIdArr) {
//			for (String itemIdStr : itemIdArr) {
//				Long itemId = Long.valueOf(itemIdStr);
//				Service service = serviceRepository.findOne(itemId);
//				if (service != null) {
//					injectionService.inInjection(service,
//							batchBean.getPlatformId(), batchBean.getPriority(),
//							currentTime, currentTime);
//				}
//			}
//		}
//		return new BaseResult();
//	}
//
//	@RequestMapping(value = { "batchOutInjection" }, method = RequestMethod.GET)
//	public String batchOutInjection(Model model,
//			@RequestParam(value = "itemType") Integer itemType,
//			@RequestParam(value = "itemIds") String itemIds) {
//
//		model.addAttribute("itemType", itemType);
//		model.addAttribute("itemIds", itemIds);
//
//		return "service/service/batchOutInjection";
//	}
//
//	@RequestMapping(value = { "batchOutInjection" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
//	@ResponseBody
//	public BaseResult batchOutInjection(
//			@RequestBody BatchInjectionBean batchBean) {
//		Integer itemType = batchBean.getItemType();
//		String itemIds = batchBean.getItemIds();
//		if (itemType == null || StringUtils.isEmpty(itemIds)) {
//			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
//		}
//		Date currentTime = new Date();
//		String[] itemIdArr = itemIds.split(",");
//		if (null != itemIdArr) {
//			for (String itemIdStr : itemIdArr) {
//				Long itemId = Long.valueOf(itemIdStr);
//				Service service = serviceRepository.findOne(itemId);
//				if (service != null) {
//					injectionService.outInjection(service,
//							service.getPlatformId(), batchBean.getPriority(),
//							currentTime, currentTime);
//				}
//			}
//		}
//		return new BaseResult();
//	}
//	
//	@RequestMapping(value = { "resetInjectionStatus" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
//	@ResponseBody
//	public BaseResult resetInjectionStatus(
//			@RequestBody BatchStatusBean batchBean) {
//		Integer itemType = batchBean.getItemType();
//		String itemIds = batchBean.getItemIds();
//		if (itemType == null || StringUtils.isEmpty(itemIds)) {
//			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
//		}
//		String[] itemIdArr = itemIds.split(",");
//		if (null != itemIdArr) {
//			for (String itemIdStr : itemIdArr) {
//				Long itemId = Long.valueOf(itemIdStr);
//				Service service = serviceRepository.findOne(itemId);
//				if (service != null) {
//					service.setInjectionStatus(InjectionStatusEnum.WAIT
//							.getKey());
//					serviceRepository.save(service);
//				}
//			}
//		}
//		return new BaseResult();
//	}
//
//
//	@RequestMapping(value = { "batchToAdd" }, method = RequestMethod.GET)
//	public String toBatchToAlbum(Model model, HttpServletRequest request,
//								 @RequestParam(value = "itemType") Integer itemType,
//								 @RequestParam(value = "itemIds") String itemIds,
//								 @RequestParam(value = "serviceId") Long serviceId) {
//
//		model.addAttribute("serviceId", serviceId);
//		model.addAttribute("itemType", itemType);
//		model.addAttribute("itemIds", itemIds);
//		List<Service> services = serviceRepository.findAll();
//		model.addAttribute("services", services);
//		model.addAttribute("statusEnum", OnlineStatusEnum.values());
//
//		return "service/service/batchToAdd";
//	}
//
//	@RequestMapping(value = { "batchToAdd" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
//	@ResponseBody
//	public BaseResult batchToAdd(
//			@RequestBody BatchServiceBean batchBean) {
//		Long serviceId = batchBean.getServiceId();
//		Integer itemType = batchBean.getItemType();
//		String itemIds = batchBean.getItemIds();
//		Integer status = batchBean.getStatus();
//		Integer sortIndex = batchBean.getSortIndex();
//
//		if (itemType == null || StringUtils.isEmpty(itemIds)) {
//			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
//		}
//		String[] itemIdArr = itemIds.split(",");
//		List<String> resMsg = new ArrayList<>();
//		if (null != itemIdArr) {
//			for (String itemIdStr : itemIdArr) {
//				Long itemId = Long.valueOf(itemIdStr);
//                Map<String, String> res = saveServiceItem(itemType, itemId, serviceId, status, sortIndex);
//                sortIndex = Integer.valueOf(res.get("sortIndex"));
//                resMsg.add(res.get("resMsg"));
//			}
//		}
//        BaseResult baseResult = new BaseResult();
//		StringBuffer str = new StringBuffer();
//		if (resMsg.size() > 0) {
//            for (String s : resMsg) {
//                if (StringUtils.isNotEmpty(s)) {
//                    str.append(s + "<br/>");
//                }
//            }
//        }
//        baseResult.setMessage(str.toString());
//		return baseResult;
//	}
//
//    private Map<String, String> saveServiceItem(Integer itemType, Long itemId, Long serviceId, Integer status, Integer sortIndex) {
//        ServiceItem serviceItemOne = serviceItemRepository.findOneByItemIdAndItemTypeAndServiceId(itemId, itemType, serviceId);
//        Integer itemStatus = OnlineStatusEnum.WAIT.getKey();
//        Map<String, String> result = new HashMap<>();
//        if (itemType == ItemTypeEnum.PROGRAM.getKey()) {
//            Program program = programRepository.findOne(itemId);
//            itemStatus = program.getStatus();
//            if (program.getInjectionStatus() != InjectionStatusEnum.INJECTED.getKey()) {
//                result.put("resMsg", "节目ID：" + program.getId() + ", 节目名称： " + program.getName() + " , 的分发状态为： "
//                        + InjectionStatusEnum.getEnumByKey(program.getInjectionStatus()).getValue());
//                result.put("sortIndex", String.valueOf(sortIndex));
//                return result;
//            }
//            if (serviceItemOne == null) {
//                serviceItemOne = new ServiceItem();
//                serviceItemOne.setServiceId(serviceId);
//                serviceItemOne.setItemType(itemType);
//                serviceItemOne.setItemId(itemId);
//                serviceItemOne.setSiteCode(AdminGlobal.getSiteCode());
//                serviceItemOne.setStatus(status);
//                serviceItemOne.setItemStatus(itemStatus);
//                serviceItemOne.setSortIndex(sortIndex++);
//            } else {
//                serviceItemOne.setServiceId(serviceId);
//                serviceItemOne.setStatus(status);
//                serviceItemOne.setItemStatus(itemStatus);
//            }
//            serviceItemRepository.save(serviceItemOne);
//            result.put("sortIndex", String.valueOf(sortIndex));
//            return result;
//        } else if (itemType == ItemTypeEnum.SERIES.getKey()) {
//            Series series = seriesRepository.findOne(itemId);
//            itemStatus = series.getStatus();
//
//            if (series.getInjectionStatus() != InjectionStatusEnum.INJECTED.getKey()) {
//                result.put("resMsg", "剧集ID：" + series.getId() + ", 剧集名称： " + series.getName() + " , 的分发状态为： "
//                        + InjectionStatusEnum.getEnumByKey(series.getInjectionStatus()));
//                result.put("sortIndex", String.valueOf(sortIndex));
//                return result;
//            }
//            Boolean flag = true;
//            List<ServiceItem> serviceItems = new ArrayList<>();
//            if (serviceItemOne == null) {
//                serviceItemOne = new ServiceItem();
//                serviceItemOne.setServiceId(serviceId);
//                serviceItemOne.setItemType(itemType);
//                serviceItemOne.setItemId(itemId);
//                serviceItemOne.setSiteCode(AdminGlobal.getSiteCode());
//                serviceItemOne.setStatus(status);
//                serviceItemOne.setItemStatus(itemStatus);
//                serviceItemOne.setSortIndex(sortIndex++);
//
//                List<Program> programs = programRepository.findBySeriesId(itemId);
//
//                if (programs != null && programs.size() != 0) {
//                    for (Program p : programs) {
//                        if (p.getInjectionStatus() != InjectionStatusEnum.INJECTED.getKey()) {
//                            result.put("resMsg", "节目ID：" + p.getId() + ", 节目名称： " + p.getName() + " , 的分发状态为： "
//                                    + InjectionStatusEnum.getEnumByKey(p.getInjectionStatus()));
//                            flag = false;
//                            continue;
//                        }
//                        ServiceItem serviceItemTwo = serviceItemRepository.findOneByItemIdAndItemTypeAndServiceId(p.getId(), ItemTypeEnum.PROGRAM.getKey(), serviceId);
//                        if (serviceItemTwo == null) {
//                            serviceItemTwo = new ServiceItem();
//                            serviceItemTwo.setServiceId(serviceId);
//                            serviceItemTwo.setItemType(ItemTypeEnum.PROGRAM.getKey());
//                            serviceItemTwo.setItemId(p.getId());
//                            serviceItemTwo.setSiteCode(AdminGlobal.getSiteCode());
//                            serviceItemTwo.setStatus(status);
//                            serviceItemTwo.setItemStatus(p.getStatus());
//                            serviceItemOne.setSortIndex(sortIndex++);
//                        } else {
//                            serviceItemTwo.setServiceId(serviceId);
//                            serviceItemTwo.setStatus(status);
//                            serviceItemTwo.setItemStatus(p.getStatus());
//                        }
//                        serviceItems.add(serviceItemTwo);
//                        //serviceItemRepository.save(serviceItemTwo);
//                    }
//                }
//            } else {
//                serviceItemOne.setServiceId(serviceId);
//                serviceItemOne.setStatus(status);
//                serviceItemOne.setItemStatus(itemStatus);
//                result.put("sortIndex", String.valueOf(sortIndex));
//                return result;
//            }
//            if (flag) {
//                for (ServiceItem s : serviceItems) {
//                    serviceItemRepository.save(s);
//                }
//                serviceItemRepository.save(serviceItemOne);
//            }
//            result.put("sortIndex", String.valueOf(sortIndex));
//            return result;
//        }
//        return result;
//    }
//
//}
