package com.ai.epg.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.epg.product.entity.Service;
import com.ai.epg.product.entity.ServiceItem;
import com.ai.epg.product.repository.ServiceItemRepository;
import com.ai.epg.product.repository.ServiceRepository;

@org.springframework.stereotype.Service
@Transactional(readOnly = true)
public class ProductService extends AbstractService<Service, Long> {

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private ServiceItemRepository serviceItemRepository;

	@Override
	public AbstractRepository<Service, Long> getRepository() {
		return serviceRepository;
	}

	@Transactional(readOnly = false)
	public void deleteService(Service service) {
		if (service != null) {
			serviceItemRepository.deleteByServiceId(service.getId());
			serviceRepository.delete(service);
		}
	}

	@Transactional(readOnly = false)
	public void batchUpdate(Long selectServiceId, Integer status,
			Integer itemType, String itemIds, Integer sortIndex) {
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr && itemIdArr.length > 0) {
			serviceItemRepository.updateSortIndexByServiceId(selectServiceId,
					itemIdArr.length, sortIndex);

			Service service = new Service();
			service.setId(selectServiceId);
			for (int i = 0; i < itemIdArr.length; i++) {
				Long itemId = Long.valueOf(itemIdArr[i]);
				ServiceItem serviceItem = serviceItemRepository
						.findOneByServiceIdAndItemTypeAndItemId(
								selectServiceId, itemType, itemId);
				if (serviceItem == null) {
					serviceItem = new ServiceItem();
					serviceItem.setServiceId(selectServiceId);
					serviceItem.setItemType(itemType);
					serviceItem.setItemId(itemId);
					serviceItem.setStatus(status);
					serviceItem.setSortIndex(i + 1);
					serviceItemRepository.save(serviceItem);
				} else {
					serviceItem.setStatus(status);
					serviceItem.setSortIndex(i + 1);
					serviceItemRepository.save(serviceItem);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void batchUpdateMoveByAfter(Long selectServiceId, Integer sortIndex) {
		serviceItemRepository.updateSortIndexByServiceId(selectServiceId, 1,
				sortIndex);
	}

	@Transactional(readOnly = false)
	public void saveServiceItem(ServiceItem serviceItem) {
		if (serviceItem != null) {
			if (serviceItem.getId() == null
					&& serviceItem.getServiceId() != null
					&& serviceItem.getSortIndex() != null) {
				batchUpdateMoveByAfter(serviceItem.getServiceId(),
						serviceItem.getSortIndex());
			}
			serviceItemRepository.save(serviceItem);
		}
	}
}