/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.artifacts;

import org.gradle.api.internal.artifacts.ivyservice.DefaultIvyContextManager;
import org.gradle.api.internal.artifacts.ivyservice.IvyContextManager;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.DefaultVersionComparator;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.VersionComparator;
import org.gradle.api.internal.artifacts.ivyservice.moduleconverter.DefaultLocalComponentMetadataBuilder;
import org.gradle.api.internal.artifacts.ivyservice.moduleconverter.LocalComponentMetadataBuilder;
import org.gradle.api.internal.artifacts.ivyservice.moduleconverter.dependencies.DefaultDependencyDescriptorFactory;
import org.gradle.api.internal.artifacts.ivyservice.moduleconverter.dependencies.DefaultExcludeRuleConverter;
import org.gradle.api.internal.artifacts.ivyservice.moduleconverter.dependencies.DefaultLocalConfigurationMetadataBuilder;
import org.gradle.api.internal.artifacts.ivyservice.moduleconverter.dependencies.DependencyDescriptorFactory;
import org.gradle.api.internal.artifacts.ivyservice.moduleconverter.dependencies.ExcludeRuleConverter;
import org.gradle.api.internal.artifacts.ivyservice.moduleconverter.dependencies.ExternalModuleIvyDependencyDescriptorFactory;
import org.gradle.api.internal.artifacts.ivyservice.moduleconverter.dependencies.LocalConfigurationMetadataBuilder;
import org.gradle.api.internal.artifacts.ivyservice.moduleconverter.dependencies.ProjectIvyDependencyDescriptorFactory;
import org.gradle.api.internal.artifacts.transform.PrimaryInputAnnotationHandler;
import org.gradle.api.internal.artifacts.transform.WorkspaceAnnotationHandler;
import org.gradle.cache.internal.ProducerGuard;
import org.gradle.internal.instantiation.InjectAnnotationHandler;
import org.gradle.internal.nativeplatform.filesystem.FileSystem;
import org.gradle.internal.resource.ExternalResourceName;
import org.gradle.internal.resource.connector.ResourceConnectorFactory;
import org.gradle.internal.resource.local.FileResourceConnector;
import org.gradle.internal.resource.local.FileResourceRepository;
import org.gradle.internal.resource.transport.file.FileConnectorFactory;

class DependencyManagementGlobalScopeServices {
    FileResourceRepository createFileResourceRepository(FileSystem fileSystem){
        return new FileResourceConnector(fileSystem);
    }

    ImmutableModuleIdentifierFactory createModuleIdentifierFactory() {
        return new DefaultImmutableModuleIdentifierFactory();
    }

    IvyContextManager createIvyContextManager() {
        return new DefaultIvyContextManager();
    }

    ExcludeRuleConverter createExcludeRuleConverter(ImmutableModuleIdentifierFactory moduleIdentifierFactory) {
        return new DefaultExcludeRuleConverter(moduleIdentifierFactory);
    }

    VersionComparator createVersionComparator() {
        return new DefaultVersionComparator();
    }

    ExternalModuleIvyDependencyDescriptorFactory createExternalModuleDependencyDescriptorFactory(ExcludeRuleConverter excludeRuleConverter) {
        return new ExternalModuleIvyDependencyDescriptorFactory(excludeRuleConverter);
    }

    DependencyDescriptorFactory createDependencyDescriptorFactory(ExcludeRuleConverter excludeRuleConverter, ExternalModuleIvyDependencyDescriptorFactory descriptorFactory) {
        return new DefaultDependencyDescriptorFactory(
            new ProjectIvyDependencyDescriptorFactory(excludeRuleConverter),
            descriptorFactory);
    }

    LocalConfigurationMetadataBuilder createLocalConfigurationMetadataBuilder(DependencyDescriptorFactory dependencyDescriptorFactory,
                                                                              ExcludeRuleConverter excludeRuleConverter) {
        return new DefaultLocalConfigurationMetadataBuilder(dependencyDescriptorFactory, excludeRuleConverter);
    }

    LocalComponentMetadataBuilder createLocalComponentMetaDataBuilder(LocalConfigurationMetadataBuilder localConfigurationMetadataBuilder) {
        return new DefaultLocalComponentMetadataBuilder(localConfigurationMetadataBuilder);
    }

    ResourceConnectorFactory createFileConnectorFactory() {
        return new FileConnectorFactory();
    }

    ProducerGuard<ExternalResourceName> createProducerAccess() {
        return ProducerGuard.adaptive();
    }

    InjectAnnotationHandler createWorkspaceAnnotationHandler() {
        return new WorkspaceAnnotationHandler();
    }

    InjectAnnotationHandler createPrimaryInputAnnotationHandler() {
        return new PrimaryInputAnnotationHandler();
    }

}
