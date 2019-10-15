/**
 * Copyright Indra Sistemas, S.A.
 * 2013-2018 SPAIN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package digitaltwin.device.status;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.minsait.onesait.platform.digitaltwin.status.IDigitalTwinStatus;
import com.minsait.onesait.platform.digitaltwin.property.controller.OperationType;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Slf4j
public class DigitalTwinStatus implements IDigitalTwinStatus{
	
	@Getter
	@Setter
	private String Identificador;
	@Getter
	@Setter
	private OperationType operationIdentificador;
	@Getter
	@Setter
	private Integer Temperatura;
	@Getter
	@Setter
	private OperationType operationTemperatura;
	@Getter
	@Setter
	private Boolean AlarmaTemperatura;
	@Getter
	@Setter
	private OperationType operationAlarmaTemperatura;
	
	private Map<String, Class> mapClass;

	private static final Logger log = LoggerFactory.getLogger(DigitalTwinStatus.class.getName());

	@PostConstruct
	public void init() {
		//Init Operation types values
			setOperationIdentificador(OperationType.IN);
			setOperationTemperatura(OperationType.IN);
			setOperationAlarmaTemperatura(OperationType.IN);
		
		mapClass = new HashMap<String, Class>();
			mapClass.put("Identificador", String.class);
			mapClass.put("Temperatura", Integer.class);
			mapClass.put("AlarmaTemperatura", Boolean.class);
	}

	@Override
	public Boolean validate(OperationType operationType, String property) {
		try {
			Class cls = this.getClass();
			Method method = cls.getDeclaredMethod("getOperation"+property.substring(0, 1).toUpperCase() + property.substring(1), null);
			OperationType operation = (OperationType) method.invoke(this,null);
			
			if(operation.equals(operationType) || operation.equals(OperationType.IN_OUT)) {
				return true;
			}
			return false;
		}catch (Exception e) {
			log.error("Validation of property "+ property + " failed.", e);
			return false;
		} 
	}

	@Override
	public Object getProperty(String property) {
		try {
			Class cls = this.getClass();
			Method method = cls.getMethod("get"+property.substring(0, 1).toUpperCase() + property.substring(1), null);
			
			return method.invoke(this, new Class[]{});

		}catch (Exception e) {
			log.error("get property "+ property + " failed.", e);
			return null;
		} 
	}

	@Override
	public void setProperty(String property, Object value) {
		try {
			Class cls = this.getClass();
			
			Method method = cls.getMethod("set"+property.substring(0, 1).toUpperCase() + property.substring(1), mapClass.get(property));
			method.invoke(this, mapClass.get(property).cast(value));

		}catch (Exception e) {
			log.error("set property "+ property + " failed.", e);
		} 
		
	}
	
	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> properties = new HashMap<String, Object>();
		
			properties.put("Identificador",Identificador);
			properties.put("Temperatura",Temperatura);
			properties.put("AlarmaTemperatura",AlarmaTemperatura);
		
		return properties;
	}
	
	public void setOperationIdentificador(OperationType entrada){
		//TODO
	}
	public void setOperationTemperatura(OperationType entrada){
		//TODO
	}
	public void setOperationAlarmaTemperatura(OperationType entrada){
		//TODO
	}

}
